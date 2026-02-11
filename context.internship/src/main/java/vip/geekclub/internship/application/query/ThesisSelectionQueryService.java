package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.InternTable;
import vip.geekclub.internship.generated.tables.SelectorTable;
import vip.geekclub.internship.generated.tables.TeamApplicationTable;
import vip.geekclub.internship.generated.tables.TeamMemberTable;
import vip.geekclub.internship.generated.tables.ThesisSelectionTable;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.internship.application.query.dto.ThesisSelectionDetailResult;

import java.util.List;

/**
 * 选题查询服务
 */
@Service
@AllArgsConstructor
public class ThesisSelectionQueryService {

    private final DSLContext dslContext;
    private final ThesisSelectionTable thesisSelectionTable = Tables.ThesisSelection;
    private final SelectorTable selectorTable = Tables.Selector;
    private final InternTable internTable = Tables.Intern;
    private final TeamApplicationTable teamApplicationTable = Tables.TeamApplication;
    private final TeamMemberTable teamMemberTable = Tables.TeamMember;

    /**
     * 获取当前用户的选题详情
     *
     * @param internId 当前用户的实习生ID
     * @return 选题详情
     */
    public ThesisSelectionDetailResult getCurrentUserSelectionDetail(Long internId) {
        // 单次查询：实习生信息 + 选题记录（通过 JOIN 关联）
        Record record = dslContext
                .select(
                        internTable.NAME,
                        internTable.ADVISOR_NAME,
                        thesisSelectionTable.ID,
                        thesisSelectionTable.ACHIEVEMENT_TYPE,
                        thesisSelectionTable.SELECTION_TYPE
                )
                .from(internTable)
                .join(selectorTable).on(selectorTable.STUDENT_ID.eq(internTable.ID))
                .join(thesisSelectionTable).on(thesisSelectionTable.ID.eq(selectorTable.PAPER_SELECTION_ID))
                .where(internTable.ID.eq(internId))
                .fetchOne();

        if (record == null) {
            throw new BusinessException(404, "当前用户尚未选题或实习生不存在");
        }

        String internName = record.get(internTable.NAME);
        String advisorName = record.get(internTable.ADVISOR_NAME);
        Long selectionId = record.get(thesisSelectionTable.ID);
        String achievementType = record.get(thesisSelectionTable.ACHIEVEMENT_TYPE);
        String selectionType = record.get(thesisSelectionTable.SELECTION_TYPE);
        boolean isGroup = "GROUP".equals(selectionType);

        // 如果是组选题，查询结组信息
        ThesisSelectionDetailResult.TeamInfo teamInfo = null;
        if (isGroup) {
            teamInfo = getTeamInfo(selectionId);
        }

        return new ThesisSelectionDetailResult(
                internName,
                isGroup,
                advisorName,
                achievementType,
                teamInfo
        );
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
}
