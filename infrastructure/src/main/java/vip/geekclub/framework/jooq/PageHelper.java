package vip.geekclub.framework.jooq;

import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.SelectLimitStep;

import static org.jooq.impl.DSL.count;

/**
 * JOOQ 分页查询工具类
 */
public class PageHelper {

    public static final org.jooq.Field<Integer> TOTAL_COUNT = count().over().as("total");

    private static final int DEFAULT_PAGE_NUM = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int MAX_PAGE_SIZE = 100;

    public static <T> PageResult<T> page(SelectLimitStep<?> query, PageQuery pageQuery, RecordMapper<? super Record, T> mapper) {
        int pageNum = pageQuery.pageNum() != null && pageQuery.pageNum() > 0 ? pageQuery.pageNum() : DEFAULT_PAGE_NUM;
        int pageSize = pageQuery.pageSize() != null && pageQuery.pageSize() > 0
                ? Math.min(pageQuery.pageSize(), MAX_PAGE_SIZE)
                : DEFAULT_PAGE_SIZE;

        var list = query.limit(pageSize).offset((pageNum - 1) * pageSize).fetch();
        var total = list.isEmpty() ? 0L : list.getFirst().get(TOTAL_COUNT);
        return new PageResult<T>(list.map(mapper), total, pageNum, pageSize);
    }
}
