package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import vip.geekclub.framework.jooq.PageHelper;
import vip.geekclub.framework.jooq.PageResult;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.*;
import org.springframework.stereotype.Service;
import vip.geekclub.internship.application.query.dto.ThesisSelectionListQuery;
import vip.geekclub.internship.application.query.dto.ThesisSelectionListResult;

import java.util.List;

import static org.jooq.impl.DSL.count;

/**
 * 论文选题列表查询服务
 * 专门处理论文选题列表相关的查询
 */
@Service
@AllArgsConstructor
public class ThesisSelectionListQueryService {

    private final DSLContext dslContext;
    private final ThesisSelectionTable thesisSelectionTable = Tables.ThesisSelection;
    private final InternTable internTable = Tables.Intern;
    private final ThesisTable thesisTable = Tables.Thesis;
    private final SelectorTable selectorTable = Tables.Selector;
    private static final Field<Integer> TOTAL_COUNT = count().over().as("total");

    /**
     * 获取所有论文选择结果列表（不分页，无条件）
     *
     * @return 所有论文选择结果列表
     */
    public List<ThesisSelectionListResult> getAllThesisSelectionList() {
        // 构建查询，不带任何条件和分页
        var list = dslContext.select(
                        thesisSelectionTable.ID,
                        thesisSelectionTable.THESIS_ID,
                        thesisSelectionTable.ACHIEVEMENT_TYPE,
                        thesisSelectionTable.SELECTION_TYPE,
                        thesisTable.TITLE,
                        internTable.as("creator").NAME,
                        internTable.as("creator").CLASS_NAME,
                        internTable.as("creator").STUDENT_NO,
                        internTable.as("creator").ADVISOR_NAME,
                        DSL.field("group_concat({0})", String.class, internTable.NAME).as("GroupMember")
                )
                .from(thesisSelectionTable)
                .join(internTable.as("creator")).on(thesisSelectionTable.CREATOR_ID.eq(internTable.as("creator").ID))
                .join(thesisTable).on(thesisTable.ID.eq(thesisSelectionTable.THESIS_ID))
                .leftJoin(selectorTable).on(selectorTable.PAPER_SELECTION_ID.eq(thesisSelectionTable.ID))
                .leftJoin(internTable).on(internTable.ID.eq(selectorTable.STUDENT_ID))
                .groupBy(
                        thesisSelectionTable.ID,
                        thesisSelectionTable.THESIS_ID,
                        thesisSelectionTable.ACHIEVEMENT_TYPE,
                        thesisSelectionTable.SELECTION_TYPE,
                        thesisTable.TITLE,
                        internTable.as("creator").CLASS_NAME,
                        internTable.as("creator").ADVISOR_NAME,
                        internTable.as("creator").NAME,
                        internTable.as("creator").STUDENT_NO
                )
                .orderBy(thesisSelectionTable.ID.asc());

        return list.fetch(r -> new ThesisSelectionListResult(
                r.get(thesisSelectionTable.ID),
                r.get(thesisSelectionTable.THESIS_ID),
                r.get(thesisTable.TITLE),
                r.get(thesisSelectionTable.ACHIEVEMENT_TYPE),
                r.get(thesisSelectionTable.SELECTION_TYPE),
                r.get(internTable.as("creator").NAME),
                r.get(internTable.as("creator").STUDENT_NO),
                r.get(internTable.as("creator").CLASS_NAME),
                r.get(internTable.as("creator").ADVISOR_NAME),
                r.get("GroupMember", String.class)
        ));
    }

    /**
     * 获取论文选择结果列表（分页）
     */
    public PageResult<ThesisSelectionListResult> getThesisSelectionList(ThesisSelectionListQuery query) {
        // 构建查询条件
        var list = dslContext.select(
                        thesisSelectionTable.ID,
                        thesisSelectionTable.THESIS_ID,
                        thesisSelectionTable.ACHIEVEMENT_TYPE,
                        thesisSelectionTable.SELECTION_TYPE,
                        thesisTable.TITLE,
                        internTable.as("creator").NAME,
                        internTable.as("creator").CLASS_NAME,
                        internTable.as("creator").STUDENT_NO,
                        internTable.as("creator").ADVISOR_NAME,
                        DSL.field("group_concat({0})", String.class, internTable.NAME).as("GroupMember"),
                        TOTAL_COUNT
                )
                .from(thesisSelectionTable)
                .join(internTable.as("creator")).on(thesisSelectionTable.CREATOR_ID.eq(internTable.as("creator").ID))
                .join(thesisTable).on(thesisTable.ID.eq(thesisSelectionTable.THESIS_ID))
                .leftJoin(selectorTable).on(selectorTable.PAPER_SELECTION_ID.eq(thesisSelectionTable.ID))
                .leftJoin(internTable).on(internTable.ID.eq(selectorTable.STUDENT_ID))
                .where(DSL.and(
                        query.thesisId() != null ? thesisSelectionTable.THESIS_ID.eq(query.thesisId()) : null,
                        query.className() != null ? internTable.CLASS_NAME.eq(query.className()) : null,
                        query.advisorName() != null ? internTable.as("creator").ADVISOR_NAME.eq(query.advisorName()) : null,
                        query.studentName() != null ? thesisSelectionTable.ID.in(
                                DSL.selectDistinct(selectorTable.PAPER_SELECTION_ID)
                                        .from(selectorTable)
                                        .join(internTable).on(internTable.ID.eq(selectorTable.STUDENT_ID))
                                        .where(internTable.NAME.like("%" + query.studentName() + "%"))
                        ) : null
                ))
                .groupBy(
                        thesisSelectionTable.ID,
                        thesisSelectionTable.THESIS_ID,
                        thesisSelectionTable.ACHIEVEMENT_TYPE,
                        thesisSelectionTable.SELECTION_TYPE,
                        thesisTable.TITLE,
                        internTable.as("creator").CLASS_NAME,
                        internTable.as("creator").ADVISOR_NAME,
                        internTable.as("creator").NAME,
                        internTable.as("creator").STUDENT_NO
                );

        return PageHelper.page(list, query.page(), r -> new ThesisSelectionListResult(
                r.get(thesisSelectionTable.ID),
                r.get(thesisSelectionTable.THESIS_ID),
                r.get(thesisTable.TITLE),
                r.get(thesisSelectionTable.ACHIEVEMENT_TYPE),
                r.get(thesisSelectionTable.SELECTION_TYPE),
                r.get(internTable.as("creator").NAME),
                r.get(internTable.as("creator").STUDENT_NO),
                r.get(internTable.as("creator").CLASS_NAME),
                r.get(internTable.as("creator").ADVISOR_NAME),
                r.get("GroupMember", String.class)
        ));
    }
}