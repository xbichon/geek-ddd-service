package vip.geekclub.internship.application.query;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.impl.DSL;
import vip.geekclub.support.jooq.PageHelper;
import vip.geekclub.support.jooq.PageResult;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.*;
import org.springframework.stereotype.Service;
import vip.geekclub.internship.application.query.dto.SelectionPageQuery;
import vip.geekclub.internship.application.query.dto.SelectionItemResult;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    private final TeamApplicationTable teamApplicationTable = Tables.TeamApplication;
    private final TeamMemberTable teamMemberTable = Tables.TeamMember;

    // 基础查询字段
    private final Field<?>[] commonSelectFields = new Field<?>[]{
            thesisSelectionTable.ID,
            thesisSelectionTable.THESIS_ID,
            thesisSelectionTable.ACHIEVEMENT_TYPE,
            thesisSelectionTable.SELECTION_TYPE,
            thesisTable.TITLE,
            internTable.as("creator").NAME,
            internTable.as("creator").CLASS_NAME,
            internTable.as("creator").STUDENT_NO,
            internTable.as("creator").ADVISOR_NAME,
            // 结组原因
            teamApplicationTable.REASON.as("CancelReason"),
            // 组员及职责：姓名(职责)
            DSL.field("group_concat(distinct concat({0}, '(', {1}, ')') order by {0} separator ', ')",
                    String.class, internTable.as("member").NAME, teamMemberTable.RESPONSIBILITY).as("GroupMembersWithDuty")
    };


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
                // 关联结组申请获取原因和组员职责
                .leftJoin(teamApplicationTable).on(teamApplicationTable.THESIS_SELECTION_ID.eq(thesisSelectionTable.ID))
                .leftJoin(teamMemberTable).on(teamMemberTable.TEAM_APPLICATION_ID.eq(teamApplicationTable.ID))
                .leftJoin(internTable.as("member")).on(internTable.as("member").ID.eq(teamMemberTable.STUDENT_ID))
                .groupBy(
                        thesisSelectionTable.ID,
                        thesisSelectionTable.THESIS_ID,
                        thesisSelectionTable.ACHIEVEMENT_TYPE,
                        thesisSelectionTable.SELECTION_TYPE,
                        thesisTable.TITLE,
                        internTable.as("creator").CLASS_NAME,
                        internTable.as("creator").ADVISOR_NAME,
                        internTable.as("creator").NAME,
                        internTable.as("creator").STUDENT_NO,
                        teamApplicationTable.REASON
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
                // 关联结组申请获取原因和组员职责
                .leftJoin(teamApplicationTable).on(teamApplicationTable.THESIS_SELECTION_ID.eq(thesisSelectionTable.ID))
                .leftJoin(teamMemberTable).on(teamMemberTable.TEAM_APPLICATION_ID.eq(teamApplicationTable.ID))
                .leftJoin(internTable.as("member")).on(internTable.as("member").ID.eq(teamMemberTable.STUDENT_ID))
                .where(DSL.and(
                        query.thesisId() != null ? thesisSelectionTable.THESIS_ID.eq(query.thesisId()) : null,
                        query.className() != null ? internTable.as("creator").CLASS_NAME.eq(query.className()) : null,
                        query.advisorName() != null ? internTable.as("creator").ADVISOR_NAME.eq(query.advisorName()) : null,
                        query.studentName() != null ? thesisSelectionTable.ID.in(
                                DSL.selectDistinct(teamApplicationTable.THESIS_SELECTION_ID)
                                        .from(teamApplicationTable)
                                        .join(teamMemberTable).on(teamMemberTable.TEAM_APPLICATION_ID.eq(teamApplicationTable.ID))
                                        .join(internTable).on(internTable.ID.eq(teamMemberTable.STUDENT_ID))
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
                        internTable.as("creator").STUDENT_NO,
                        teamApplicationTable.REASON
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
        String selectionType = r.get(thesisSelectionTable.SELECTION_TYPE);
        String creatorName = r.get(internTable.as("creator").NAME);
        String groupMembersWithDuty = r.get("GroupMembersWithDuty", String.class);
        String cancelReason = r.get("CancelReason", String.class);

        // 如果不是小组，组员为空；如果是小组，剔除当前创建者
        if (!"GROUP".equals(selectionType)) {
            groupMembersWithDuty = null;
        } else if (groupMembersWithDuty != null && creatorName != null) {
            String finalCreatorName = creatorName.trim();
            groupMembersWithDuty = Arrays.stream(groupMembersWithDuty.split(","))
                    .map(String::trim)
                    .filter(member -> !member.startsWith(finalCreatorName + "("))
                    .collect(Collectors.joining(", "));
        }

        return new SelectionItemResult(
                r.get(thesisSelectionTable.ID),
                r.get(thesisSelectionTable.THESIS_ID),
                r.get(thesisTable.TITLE),
                r.get(thesisSelectionTable.ACHIEVEMENT_TYPE),
                selectionType,
                creatorName,
                r.get(internTable.as("creator").STUDENT_NO),
                r.get(internTable.as("creator").CLASS_NAME),
                r.get(internTable.as("creator").ADVISOR_NAME),
                groupMembersWithDuty,
                cancelReason
        );
    }
}