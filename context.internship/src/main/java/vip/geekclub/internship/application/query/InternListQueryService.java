package vip.geekclub.internship.application.query;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.jooq.PageHelper;
import vip.geekclub.framework.jooq.PageResult;
import vip.geekclub.internship.application.query.dto.InternItemResult;
import vip.geekclub.internship.application.query.dto.InternPageQuery;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.InternTable;
import vip.geekclub.internship.generated.tables.SelectorTable;


/**
 * 实习生列表查询服务
 */
@Service
@RequiredArgsConstructor
public class InternListQueryService {

    private final DSLContext dslContext;
    private final InternTable internTable = Tables.Intern;
    private final SelectorTable selectorTable = Tables.Selector;

    /**
     * 分页查询实习生列表
     *
     * @param query 查询参数
     * @return 分页结果
     */
    public PageResult<InternItemResult> findPage(InternPageQuery query) {
        // 构建查询 - 使用 LEFT JOIN 一次性获取是否选题状态
        var listQuery = dslContext
                .select(
                        internTable.ID,
                        internTable.NAME,
                        internTable.STUDENT_NO,
                        internTable.CLASS_NAME,
                        internTable.ADVISOR_NAME,
                        selectorTable.ID.isNotNull().as("selected"),
                        PageHelper.TOTAL_COUNT
                )
                .from(internTable)
                .leftJoin(selectorTable).on(selectorTable.STUDENT_ID.eq(internTable.ID))
                .where(DSL.and(
                        query.className() != null ? internTable.CLASS_NAME.eq(query.className()) : null,
                        query.advisorName() != null ? internTable.ADVISOR_NAME.eq(query.advisorName()) : null,
                        query.studentName() != null ? internTable.NAME.like("%" + query.studentName() + "%") : null,
                        query.studentNo() != null ? internTable.STUDENT_NO.eq(query.studentNo()) : null,
                        query.selected() != null ? (
                                query.selected()
                                        ? selectorTable.ID.isNotNull()
                                        : selectorTable.ID.isNull()
                        ) : null
                ))
                .orderBy(internTable.ID.asc());

        return PageHelper.page(listQuery, query, this::mapToInternItem);
    }

    /**
     * 将数据库记录映射为 InternItemResult 对象
     */
    private InternItemResult mapToInternItem(Record r) {
        return new InternItemResult(
                r.get(internTable.ID),
                r.get(internTable.NAME),
                r.get(internTable.STUDENT_NO),
                r.get(internTable.CLASS_NAME),
                r.get(internTable.ADVISOR_NAME),
                r.get("selected", Boolean.class)
        );
    }
}
