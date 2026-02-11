package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.internship.Tables;
import org.jooq.generated.internship.tables.InternTable;
import org.jooq.generated.internship.tables.SelectorTable;
import org.jooq.generated.internship.tables.TeamApplicationTable;
import org.jooq.generated.internship.tables.TeamMemberTable;
import org.jooq.generated.internship.tables.ThesisSelectionTable;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.command.CommandContext;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.internship.application.query.dto.ThesisSelectionDetailResult;

import java.util.List;
import java.util.stream.Collectors;

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
     * @return 选题详情
     */
    public ThesisSelectionDetailResult getCurrentUserSelectionDetail() {
        // 1. 获取当前登录用户的authId
        var principal = CommandContext.getCurrentPrincipal();
        String authId = principal.authId();

        // 2. 查询当前用户的实习生信息
        var internRecord = dslContext
                .select(internTable.ID, internTable.NAME, internTable.ADVISOR_NAME)
                .from(internTable)
                .where(internTable.AUTH_ID.eq(authId))
                .fetchOne();

        if (internRecord == null) {
            throw new BusinessException(404, "当前用户不是实习生");
        }

        Long internId = internRecord.get(internTable.ID);
        String internName = internRecord.get(internTable.NAME);
        String advisorName = internRecord.get(internTable.ADVISOR_NAME);

        // 3. 查询该实习生的选题记录ID
        var selectorRecord = dslContext
                .select(selectorTable.PAPER_SELECTION_ID)
                .from(selectorTable)
                .where(selectorTable.STUDENT_ID.eq(internId))
                .fetchOne();

        if (selectorRecord == null) {
            throw new BusinessException(404, "当前用户尚未选题");
        }

        Long selectionId = selectorRecord.get(selectorTable.PAPER_SELECTION_ID);

        // 4. 查询选题记录详情
        var selectionRecord = dslContext
                .select(thesisSelectionTable.ID, thesisSelectionTable.ACHIEVEMENT_TYPE, thesisSelectionTable.SELECTION_TYPE)
                .from(thesisSelectionTable)
                .where(thesisSelectionTable.ID.eq(selectionId))
                .fetchOne();

        if (selectionRecord == null) {
            throw new BusinessException(404, "选题记录不存在");
        }

        String achievementType = selectionRecord.get(thesisSelectionTable.ACHIEVEMENT_TYPE);
        String selectionType = selectionRecord.get(thesisSelectionTable.SELECTION_TYPE);
        boolean isGroup = "GROUP".equals(selectionType);

        // 5. 如果是组选题，查询结组信息
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
     * 获取结组信息
     *
     * @param selectionId 选题记录ID
     * @return 结组信息
     */
    private ThesisSelectionDetailResult.TeamInfo getTeamInfo(Long selectionId) {
        // 查询结组申请
        var teamAppRecord = dslContext
                .select(teamApplicationTable.ID, teamApplicationTable.REASON)
                .from(teamApplicationTable)
                .where(teamApplicationTable.THESIS_SELECTION_ID.eq(selectionId))
                .fetchOne();

        if (teamAppRecord == null) {
            return null;
        }

        Long teamAppId = teamAppRecord.get(teamApplicationTable.ID);
        String reason = teamAppRecord.get(teamApplicationTable.REASON);

        // 查询组员列表
        var memberRecords = dslContext
                .select(teamMemberTable.STUDENT_ID, teamMemberTable.RESPONSIBILITY)
                .from(teamMemberTable)
                .where(teamMemberTable.TEAM_APPLICATION_ID.eq(teamAppId))
                .fetch();

        // 获取所有学生ID
        List<Long> studentIds = memberRecords.stream()
                .map(r -> r.get(teamMemberTable.STUDENT_ID))
                .toList();

        // 查询学生姓名
        var internRecords = dslContext
                .select(internTable.ID, internTable.NAME)
                .from(internTable)
                .where(internTable.ID.in(studentIds))
                .fetch();

        // 构建学生ID到姓名的映射
        var internNameMap = internRecords.stream()
                .collect(Collectors.toMap(
                        r -> r.get(internTable.ID),
                        r -> r.get(internTable.NAME)
                ));

        // 构建组员信息列表
        List<ThesisSelectionDetailResult.TeamMemberInfo> members = memberRecords.stream()
                .map(r -> {
                    Long studentId = r.get(teamMemberTable.STUDENT_ID);
                    String name = internNameMap.getOrDefault(studentId, "未知");
                    String responsibility = r.get(teamMemberTable.RESPONSIBILITY);
                    return new ThesisSelectionDetailResult.TeamMemberInfo(name, responsibility);
                })
                .toList();

        return new ThesisSelectionDetailResult.TeamInfo(reason, members);
    }
}
