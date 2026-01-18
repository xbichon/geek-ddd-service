package vip.geekclub.framework.domain;

/**
 * 聚合根接口
 * <p>
 * 在DDD架构中，聚合根是聚合的入口点，负责维护聚合内部的一致性边界。
 * 聚合根标识了领域对象在业务概念上的边界，确保业务规则的一致性。
 * <p>
 * 主要职责：
 * <ul>
 * <li>维护聚合内部的一致性</li>
 * <li>提供聚合的唯一标识符</li>
 * <li>封装聚合内部的业务逻辑</li>
 * </ul>
 *
 * @param <T> 聚合标识符的类型
 * @author leo
 * @since 1.0
 */
public interface AggregateRoot<T> extends Aggregate<T> {

}
