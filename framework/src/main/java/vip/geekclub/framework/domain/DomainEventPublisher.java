package vip.geekclub.framework.domain;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import vip.geekclub.framework.exception.BusinessLogicException;

@Slf4j
@Component
public class DomainEventPublisher implements ApplicationEventPublisher {

    private final ApplicationContext applicationContext;

    public DomainEventPublisher(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void publishEvent(@NonNull Object event) {
        if (!(event instanceof DomainEvent domainEvent))
            throw new BusinessLogicException("发布事件对象必须是领域事件: " + event.getClass().getSimpleName());

        publishAfterCommit(domainEvent);
    }

    public void publishEvent(@NonNull DomainEvent event) {
        publishAfterCommit(event);
    }

    /**
     * 事务提交后发布领域事件
     * 要求必须在事务中调用，保证业务操作和事件发布的事务一致性
     */
    public void publishAfterCommit(@NonNull DomainEvent event) {
        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new BusinessLogicException("发布领域事件必须在事务中进行: " + event.getClass().getSimpleName());
        }

        // 在事务中，注册同步器延迟到事务提交后发布
        TransactionSynchronizationManager.registerSynchronization(
                new DomainEventTransactionSynchronization(event));
    }

    /**
     * 事务同步器实现
     * 每个事件独立注册，确保在事务提交后发布
     */
    private class DomainEventTransactionSynchronization implements TransactionSynchronization {

        private final DomainEvent event;
        private static final int MAX_RETRY_COUNT = 3;

        public DomainEventTransactionSynchronization(DomainEvent event) {
            this.event = event;
        }

        @Override
        public void afterCommit() {
            publishWithRetry(event);
        }

        @Override
        public void afterCompletion(int status) {
            if (status == STATUS_ROLLED_BACK) {
                log.debug("事务回滚，丢弃领域事件: {}", event.getClass().getSimpleName());
            }
        }

        /**
         * 带重试机制的事件发布
         */
        private void publishWithRetry(DomainEvent event) {
            int attempt = 0;
            while (attempt <= MAX_RETRY_COUNT) {
                try {
                    applicationContext.publishEvent(event);
                    log.debug("事务提交后成功发布领域事件: {}", event.getClass().getSimpleName());
                    return; // 成功则退出
                } catch (Exception e) {
                    attempt++;
                    if (attempt > MAX_RETRY_COUNT) {
                        log.error("发布领域事件失败，已达最大重试次数({}): {}", MAX_RETRY_COUNT,
                                event.getClass().getSimpleName(), e);
                        // 这里可以扩展为将事件放入死信队列
                        // deadLetterQueue.save(event);
                    } else {
                        log.warn("发布领域事件失败，第{}次重试: {}", attempt,
                                event.getClass().getSimpleName(), e);
                        try {
                            Thread.sleep(100L * attempt); // 指数退避简化版
                        } catch (InterruptedException ie) {
                            Thread.currentThread().interrupt();
                            log.error("重试等待被中断", ie);
                            break;
                        }
                    }
                }
            }
        }
    }
}
