package vip.geekclub.framework.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.util.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import vip.geekclub.framework.exception.BusinessLogicException;
import vip.geekclub.framework.utils.ApplicationUtil;


@Slf4j
@Component
@AllArgsConstructor
public class DomainEventPublisher implements ApplicationEventPublisher {

    private ApplicationContext applicationEventPublisher;

    /**
     * 获取实例
     */
    @Getter
    private static final Lazy<@NonNull DomainEventPublisher> instance =
            Lazy.of(() -> ApplicationUtil.getBean(DomainEventPublisher.class));

    public void publishEvent(@NonNull Object event) {
        if (event instanceof DomainEvent) {
            addEvent((DomainEvent) event);
        } else {
            throw new BusinessLogicException("领域事件必须实现 DomainEvent 接口");
        }
    }

    /**
     * 添加领域事件
     * 如果在事务中，注册事务同步器延迟发布；否则直接发布
     */
    private void addEvent(DomainEvent event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            // 在事务中，为每个事件注册独立的同步器
            TransactionSynchronizationManager.registerSynchronization(
                    new DomainEventTransactionSynchronization(event));
        } else {
            // 不在事务中，直接发布
            applicationEventPublisher.publishEvent(event);
        }
    }

    /**
     * 事务同步器实现
     * 每个事件独立注册，确保在事务提交后发布
     */
    private class DomainEventTransactionSynchronization implements TransactionSynchronization {

        private final DomainEvent event;

        public DomainEventTransactionSynchronization(DomainEvent event) {
            this.event = event;
        }

        @Override
        public void afterCommit() {
            try {
                applicationEventPublisher.publishEvent(event);
                log.debug("事务提交后成功发布领域事件: {}", event.getClass().getSimpleName());
            } catch (Exception e) {
                log.error("发布领域事件失败: {}", event.getClass().getSimpleName(), e);
            }
        }

        @Override
        public void afterCompletion(int status) {
            if (status == STATUS_ROLLED_BACK) {
                log.debug("事务回滚，丢弃领域事件: {}", event.getClass().getSimpleName());
            }
        }
    }

}
