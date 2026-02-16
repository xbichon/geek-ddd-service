package vip.geekclub.internship.application.query;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import vip.geekclub.framework.jooq.PageHelper;
import vip.geekclub.framework.jooq.PageResult;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.*;
import org.springframework.stereotype.Service;
import vip.geekclub.internship.application.query.dto.SelectionPageQuery;
import vip.geekclub.internship.application.query.dto.SelectionItemResult;

import java.util.List;

import static org.jooq.impl.DSL.count;

/**
 * 论文选题列表查询服务
 * 专门处理论文选题列表相关的查询
 */
@Service
@RequiredArgsConstructor
public class SelectionListQueryService {

    private final DSLContext dslContext;
    private final ThesisSelectionTable thesisSelectionTable = Tables.ThesisSelection;
    private final InternTable internTable = Tables.Intern;
    private final ThesisTable thesisTable = Tables.Thesis;
    private final SelectorTable selectorTable = Tables.Selector;
    private final Field<?>[] commonSelectFields= new Field<?>[] {
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
    };;


    /**
     * 获取所有论文选择结果列表（不分页，无条件）
     *
     * @return 所有论文选择结果列表
     */
    public List<SelectionItemResult> findAll() {
        // 构建查询，不带任何条件和分页
        var list = dslContext.select(commonSelectFields)
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

        return list.fetch(this::mapToSelectionItem);
    }

    /**
     * 获取论文选择结果列表（分页）
     */
    public PageResult<SelectionItemResult> findPage(SelectionPageQuery query) {
        // 构建查询条件
        var list = dslContext.select(commonSelectFields)
                .select(PageHelper.TOTAL_COUNT)
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

        return PageHelper.page(list, query, this::mapToSelectionItem);
    }

    /**
     * 将数据库记录映射为 ThesisSelectionListResult 对象
     *
     * @param r 数据库记录
     * @return ThesisSelectionListResult 对象
     */
    private SelectionItemResult mapToSelectionItem(Record r) {
        return new SelectionItemResult(
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
        );
    }
}