package vip.geekclub.internship.application.query.dto;

import java.util.List;

/**
 * 论文列表查询结果
 * 包含论文基本信息及成果形式集合
 */
public record ThesisItemResult(
        Long id,
        String title,
        Integer maxSelections,
        Integer currentSelections,
        List<String> achievementTypes
) {
}