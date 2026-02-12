package vip.geekclub.framework.jooq;

import org.jooq.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

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
public class JooqPageHelper {

    private final DSLContext dslContext;

    public JooqPageHelper(DSLContext dslContext) {
        this.dslContext = dslContext;
    }

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
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> PageResult<T> paginate(SelectLimitStep query, PageQuery pageQuery, RecordMapper mapper) {

        // 统计总数（基于已带条件的查询生成子查询）
        Long total = dslContext.selectCount()
                .from(query.asTable("t"))
                .fetchOne(0, Long.class);

        // 执行分页查询
        List<T> list = (List<T>) query
                .limit(pageQuery.getLimit())
                .offset(pageQuery.getOffset())
                .fetch()
                .map(mapper);

        return new PageResult<>(list, total, pageQuery.pageNum(), pageQuery.pageSize());
    }
}