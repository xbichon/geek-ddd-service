package vip.geekclub.framework.domain;

import lombok.NonNull;
import org.springframework.context.ApplicationEventPublisher;

/**
 * 领域事件发布器接口
 * 定义领域事件发布的核心契约，支持多种实现策略
 */
public interface DomainEventPublisher extends ApplicationEventPublisher {

    /**
     * 事务提交后发布领域事件
     * 要求必须在事务中调用，保证业务操作和事件发布的事务一致性
     *
     * @param event 领域事件
     */
    void publishAfterCommit(@NonNull DomainEvent event);

    /**
     * 发布领域事件
     * 便捷方法，内部调用publishAfterCommit
     *
     * @param event 领域事件
     */
    void publishEvent(@NonNull DomainEvent event);
}