package vip.geekclub.internship.application.query.dto;

import java.util.List;

/**
 * 分页查询结果
 */
public record PageResult<T>(
        /*
          当前页数据
         */
        List<T> records,
        
        /*
          总记录数
         */
        long total,
        
        /*
          当前页码
         */
        int pageNum,
        
        /*
          每页大小
         */
        int pageSize,
        
        /*
          总页数
         */
        int totalPages
) {
    public PageResult(List<T> records, long total, int pageNum, int pageSize) {
        this(records, total, pageNum, pageSize, 
             (int) Math.ceil((double) total / pageSize));
    }
    
    /**
     * 是否有下一页
     */
    public boolean hasNext() {
        return pageNum < totalPages;
    }
    
    /**
     * 是否有上一页
     */
    public boolean hasPrevious() {
        return pageNum > 1;
    }
}