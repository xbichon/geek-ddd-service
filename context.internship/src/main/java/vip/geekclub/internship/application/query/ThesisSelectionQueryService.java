package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.*;
import org.jooq.Record;
import org.jooq.impl.DSL;
import vip.geekclub.framework.jooq.PageHelper;
import vip.geekclub.framework.jooq.PageResult;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.*;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.internship.application.query.dto.*;

import java.util.List;

import static org.jooq.impl.DSL.count;

/**
 * 选题查询服务
 */
@Service
@AllArgsConstructor
public class ThesisSelectionQueryService {

    private final DSLContext dslContext;
    private final ThesisSelectionTable thesisSelectionTable = Tables.ThesisSelection;
    private final InternTable internTable = Tables.Intern;
    private final ThesisTable thesisTable = Tables.Thesis;
    private final TeamApplicationTable teamApplicationTable = Tables.TeamApplication;
    private final TeamMemberTable teamMemberTable = Tables.TeamMember;
    private final SelectorTable selectorTable = Tables.Selector;
    private static final Field<Integer> TOTAL_COUNT = count().over().as("total");

    /**
     * 获取当前用户的选题详情
     *
     * @param internId 当前用户的实习生ID
     * @return 选题详情
     */
    public ThesisSelectionDetailResult getCurrentUserSelectionDetail(Long internId) {
        // 1. 根据 internId 从 selectorTable 查询对应的论文选题 ID
        var selectorRecord = dslContext
                .select(selectorTable.PAPER_SELECTION_ID)
                .from(selectorTable)
                .where(selectorTable.STUDENT_ID.eq(internId))
                .fetchOne();

        if (selectorRecord == null) {
            throw new BusinessException(404, "当前用户尚未选题");
        }

        Long selectionId = selectorRecord.get(selectorTable.PAPER_SELECTION_ID);

        // 2. 根据论文选题 ID 查询论文、选题记录及创建者信息（连表查询）
        Record record = dslContext
                .select(
                        thesisSelectionTable.ID,
                        thesisSelectionTable.ACHIEVEMENT_TYPE,
                        thesisSelectionTable.SELECTION_TYPE,
                        thesisSelectionTable.CREATOR_ID,
                        thesisTable.TITLE,
                        internTable.NAME,
                        internTable.ADVISOR_NAME
                )
                .from(thesisSelectionTable)
                .join(thesisTable).on(thesisTable.ID.eq(thesisSelectionTable.THESIS_ID))
                .join(internTable).on(internTable.ID.eq(thesisSelectionTable.CREATOR_ID))
                .where(thesisSelectionTable.ID.eq(selectionId))
                .fetchOne();

        if (record == null) {
            throw new BusinessException(404, "选题记录不存在");
        }

        Long resultSelectionId = record.get(thesisSelectionTable.ID);
        String achievementType = record.get(thesisSelectionTable.ACHIEVEMENT_TYPE);
        String selectionType = record.get(thesisSelectionTable.SELECTION_TYPE);
        String thesisTitle = record.get(thesisTable.TITLE);
        String internName = record.get(internTable.NAME);
        String advisorName = record.get(internTable.ADVISOR_NAME);
        boolean isGroup = "GROUP".equals(selectionType);

        // 如果是组选题，查询结组信息
        ThesisSelectionDetailResult.TeamInfo teamInfo = null;
        if (isGroup) {
            teamInfo = getTeamInfo(resultSelectionId);
        }

        return new ThesisSelectionDetailResult(
                internName,
                isGroup,
                advisorName,
                achievementType,
                thesisTitle,
                teamInfo
        );
    }

    /**
     * 判断当前用户是否已经选题
     *
     * @param internId 当前用户的实习生ID
     * @return true-已选题，false-未选题
     */
    public boolean hasCurrentUserSelected(Long internId) {
        // 查询该学生是否在selector表中存在记录
        var record = dslContext
                .select(selectorTable.ID)
                .from(selectorTable)
                .where(selectorTable.STUDENT_ID.eq(internId))
                .limit(1)
                .fetchOne();

        return record != null;
    }

    /**
     * 获取结组信息（单次 JOIN 查询组员及姓名）
     *
     * @param selectionId 选题记录ID
     * @return 结组信息
     */
    private ThesisSelectionDetailResult.TeamInfo getTeamInfo(Long selectionId) {
        // 单次查询：结组申请 + 组员信息 + 实习生姓名
        var records = dslContext
                .select(
                        teamApplicationTable.REASON,
                        teamMemberTable.RESPONSIBILITY,
                        internTable.NAME
                )
                .from(teamApplicationTable)
                .join(teamMemberTable).on(teamMemberTable.TEAM_APPLICATION_ID.eq(teamApplicationTable.ID))
                .join(internTable).on(internTable.ID.eq(teamMemberTable.STUDENT_ID))
                .where(teamApplicationTable.THESIS_SELECTION_ID.eq(selectionId))
                .fetch();

        if (records.isEmpty()) {
            return null;
        }

        // 获取结组原因（所有记录都一样，取第一条）
        String reason = records.getFirst().get(teamApplicationTable.REASON);

        // 构建组员信息列表
        List<ThesisSelectionDetailResult.TeamMemberInfo> members = records.stream()
                .map(r -> new ThesisSelectionDetailResult.TeamMemberInfo(
                        r.get(internTable.NAME),
                        r.get(teamMemberTable.RESPONSIBILITY)
                ))
                .toList();

        return new ThesisSelectionDetailResult.TeamInfo(reason, members);
    }

    /**
     * 获取论文选择结果列表（分页）
     *
     * @param query 查询参数
     * @return 论文选择结果分页列表
     */
    public PageResult<ThesisSelectionListResult> getThesisSelectionList(ThesisSelectionListQuery query) {

        // 1. 构建查询条件
        Condition condition = DSL.and(
                query.className() != null ? internTable.CLASS_NAME.eq(query.className()) : null,
                query.advisorName() != null ? internTable.ADVISOR_NAME.eq(query.advisorName()) : null,
                query.studentName() != null ? internTable.NAME.like("%" + query.studentName() + "%") : null
        );

        // 2. 构建基础查询
        var list = dslContext.select(
                        thesisSelectionTable.ID,
                        thesisSelectionTable.THESIS_ID,
                        thesisSelectionTable.ACHIEVEMENT_TYPE,
                        thesisSelectionTable.SELECTION_TYPE,
                        thesisTable.TITLE,
                        internTable.ID.as("studentId"),
                        internTable.NAME,
                        internTable.STUDENT_NO,
                        internTable.CLASS_NAME,
                        internTable.ADVISOR_NAME,
                        TOTAL_COUNT  //窗口函数
                )
                .from(thesisSelectionTable)
                .join(thesisTable).on(thesisTable.ID.eq(thesisSelectionTable.THESIS_ID))
                .join(selectorTable).on(selectorTable.PAPER_SELECTION_ID.eq(thesisSelectionTable.ID))
                .join(internTable).on(internTable.ID.eq(selectorTable.STUDENT_ID))
                .where(condition);

        return PageHelper.page(list, query.pageQuery(), r -> new ThesisSelectionListResult(
                r.get(thesisSelectionTable.ID),
                r.get(thesisSelectionTable.THESIS_ID),
                r.get(thesisTable.TITLE),
                r.get(thesisSelectionTable.ACHIEVEMENT_TYPE),
                r.get(thesisSelectionTable.SELECTION_TYPE),
                r.get(internTable.ID),
                r.get(internTable.NAME),
                r.get(internTable.STUDENT_NO),
                r.get(internTable.CLASS_NAME),
                r.get(internTable.ADVISOR_NAME),
                List.of()));
    }
}
