package vip.geekclub.internship.application.query.dto;

/**
 * 论文选题人数统计结果
 *
 * @param thesisId        论文ID
 * @param thesisTitle     论文标题
 * @param selectionCount  已选人数
 * @param maxSelections   可选上限
 */
public record ThesisSelectionCountResult(
        Long thesisId,
        String thesisTitle,
        Integer selectionCount,
        Integer maxSelections
) {
}
