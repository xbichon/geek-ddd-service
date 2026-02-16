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


    /**
     * 获取页码（从1开始）
     */
    default int getPageNum() {
        return pageNum() != null && pageNum() > 0 ? pageNum() : 1;
    }

    /**
     * 获取每页大小
     */
    default int getPageSize() {
        return pageSize() != null && pageSize() > 0 ? Math.min(pageSize(), 100) : 10;
    }

    /**
     * 计算偏移量
     */
    default int getOffset() {
        return (getPageNum() - 1) * getLimit();
    }

    /**
     * 获取限制数量
     */
    default int getLimit() {
        return getPageSize();
    }
}