package vip.geekclub.support.jooq;

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
        int pageSize
) {
}