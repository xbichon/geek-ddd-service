package vip.geekclub.framework.jooq;

/**
 * 分页查询接口
 */
public interface PageQuery {

    /**
     * 页码（从1开始）
     */
    Integer pageNum();

    /**
     * 每页大小
     */
    Integer pageSize();
}
