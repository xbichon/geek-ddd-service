package vip.geekclub.internship.application.query.dto;

import java.util.List;

/**
 * 论文选择结果列表查询结果
 */
public record ThesisSelectionListResult(
        Long selectionId,
        Long thesisId,
        String thesisTitle,
        String achievementType,
        String selectionType,
        String studentName,
        String studentNumber,
        String className,
        String advisorName,
        String groupMembers
) {
}