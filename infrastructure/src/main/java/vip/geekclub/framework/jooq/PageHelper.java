package vip.geekclub.framework.jooq;

import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import java.util.List;

import static org.jooq.impl.DSL.count;

/**
 * JOOQ 分页查询工具类
 *
 * <p>提供通用的分页查询封装，调用方完全控制查询构建，本工具只负责：</p>
 * <ul>
 *   <li>基于带条件的查询统计总数</li>
 *   <li>执行分页查询（limit/offset）</li>
 *   <li>结果转换</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
public class PageHelper {

    public static final Field<Integer> TOTAL_COUNT = count().over().as("total");

    /**
     * 分页查询
     *
     * <p>调用方需自己将条件添加到查询中，示例：</p>
     * <pre>
     * var query = dslContext
     *     .select(...)
     *     .from(...)
     *     .join(...)
     *     .where(condition);  // 条件自己加
     *
     * return pageHelper.paginate(query, pageQuery, this::mapToDto);
     * </pre>
     *
     * @param query     带条件的完整查询（必须已包含ORDER BY，SelectLimitStep类型）
     * @param pageQuery 分页参数（pageNum从1开始）
     * @param mapper    结果映射器（Record -> DTO）
     * @param <T>       返回数据类型
     * @return 分页结果
     */
    public static  <T> PageResult<T> page(SelectLimitStep<?> query, PageQuery pageQuery, RecordMapper<Record, T> mapper) {
        var list = query.limit(pageQuery.getLimit())
                .offset(pageQuery.getOffset())
                .fetch();

        var total = list.isEmpty() ? 0L : list.getFirst().get(TOTAL_COUNT);
        var result = list.map(mapper);

        return new PageResult<>(result, total, pageQuery);
    }
}