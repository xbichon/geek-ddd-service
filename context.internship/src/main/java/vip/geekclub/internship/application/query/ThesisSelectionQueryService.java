package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.*;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.internship.application.query.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * 获取论文选择结果列表
     *
     * @param query 查询参数
     * @return 论文选择结果列表
     */
    public List<ThesisSelectionListResult> getThesisSelectionList(ThesisSelectionListQuery query) {
        // 1. 构建查询条件列表
        List<org.jooq.Condition> conditions = new ArrayList<>();

        // 2. 根据查询参数添加条件
        if (query.className() != null) {
            conditions.add(internTable.CLASS_NAME.eq(query.className()));
        }
        if (query.advisorName() != null) {
            conditions.add(internTable.ADVISOR_NAME.eq(query.advisorName()));
        }
        if (query.studentName() != null) {
            conditions.add(internTable.NAME.like("%" + query.studentName() + "%"));
        }

        // 3. 查询所有选题记录及关联信息
        var selectStep = dslContext
                .select(
                        thesisSelectionTable.ID,
                        thesisSelectionTable.THESIS_ID,
                        thesisSelectionTable.ACHIEVEMENT_TYPE,
                        thesisSelectionTable.SELECTION_TYPE,
                        thesisSelectionTable.CREATOR_ID,
                        thesisTable.TITLE,
                        internTable.NAME,
                        internTable.STUDENT_NO,
                        internTable.CLASS_NAME,
                        internTable.ADVISOR_NAME
                )
                .from(thesisSelectionTable)
                .join(thesisTable).on(thesisTable.ID.eq(thesisSelectionTable.THESIS_ID))
                .join(selectorTable).on(selectorTable.PAPER_SELECTION_ID.eq(thesisSelectionTable.ID))
                .join(internTable).on(internTable.ID.eq(selectorTable.STUDENT_ID));

        // 4. 应用查询条件
        var records = conditions.isEmpty()
                ? selectStep.fetch()
                : selectStep.where(conditions).fetch();

        // 5. 获取所有选题ID（用于批量查询选择者信息）
        List<Long> selectionIds = records.stream()
                .map(r -> r.get(thesisSelectionTable.ID))
                .distinct()
                .toList();

        // 6. 批量查询每个选题的选择者信息
        Map<Long, List<SelectorResult>> selectorsMap = batchGetSelectors(selectionIds);

        // 7. 组装结果（按选题ID分组，每个选题只返回一条记录，包含所有选择者）
        Map<Long, Record> uniqueSelections = records.stream()
                .collect(Collectors.toMap(
                        r -> r.get(thesisSelectionTable.ID),
                        r -> r,
                        (existing, replacement) -> existing
                ));

        return uniqueSelections.values().stream()
                .map(r -> {
                    Long selectionId = r.get(thesisSelectionTable.ID);
                    String selectionType = r.get(thesisSelectionTable.SELECTION_TYPE);
                    boolean isGroup = "GROUP".equals(selectionType);

                    return new ThesisSelectionListResult(
                            selectionId,
                            r.get(thesisSelectionTable.THESIS_ID),
                            r.get(thesisTable.TITLE),
                            r.get(thesisSelectionTable.ACHIEVEMENT_TYPE),
                            selectionType,
                            isGroup,
                            r.get(thesisSelectionTable.CREATOR_ID),
                            r.get(internTable.NAME),
                            r.get(internTable.STUDENT_NO),
                            r.get(internTable.CLASS_NAME),
                            r.get(internTable.ADVISOR_NAME),
                            selectorsMap.getOrDefault(selectionId, List.of())
                    );
                })
                .toList();
    }

    /**
     * 批量获取选择者信息
     *
     * @param selectionIds 选题记录ID列表
     * @return 选题ID到选择者列表的映射
     */
    private Map<Long, List<SelectorResult>> batchGetSelectors(List<Long> selectionIds) {
        if (selectionIds.isEmpty()) {
            return Map.of();
        }

        // 查询所有选择者信息
        var records = dslContext
                .select(
                        selectorTable.PAPER_SELECTION_ID,
                        internTable.ID,
                        internTable.NAME,
                        internTable.STUDENT_NO,
                        internTable.CLASS_NAME
                )
                .from(selectorTable)
                .join(internTable).on(internTable.ID.eq(selectorTable.STUDENT_ID))
                .where(selectorTable.PAPER_SELECTION_ID.in(selectionIds))
                .fetch();

        // 按选题ID分组
        return records.stream()
                .collect(Collectors.groupingBy(
                        r -> r.get(selectorTable.PAPER_SELECTION_ID),
                        Collectors.mapping(
                                r -> new SelectorResult(
                                        r.get(internTable.ID),
                                        r.get(internTable.NAME),
                                        r.get(internTable.STUDENT_NO),
                                        r.get(internTable.CLASS_NAME)
                                ),
                                Collectors.toList()
                        )
                ));
    }
}
