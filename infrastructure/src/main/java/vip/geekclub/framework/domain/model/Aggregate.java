package vip.geekclub.framework.domain.model;

public interface Aggregate<T> {
    /**
     * 获取聚合根ID
     *
     * @return 聚合根ID
     */
    T getId();
}
