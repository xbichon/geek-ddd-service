package vip.geekclub.internship.application.query.dto;

/**
 * 分页查询参数基类
 */
public record PageQuery(
        /**
         * 页码（从1开始）
         */
        Integer pageNum,
        
        /**
         * 每页大小
         */
        Integer pageSize
) {
    public PageQuery {
        // 默认值处理
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        // 限制最大每页大小
        if (pageSize > 100) {
            pageSize = 100;
        }
    }
    
    /**
     * 计算偏移量
     */
    public int getOffset() {
        return (pageNum - 1) * pageSize;
    }
    
    /**
     * 获取限制数量
     */
    public int getLimit() {
        return pageSize;
    }
}