package vip.geekclub.internship.application.query.dto;

/**
 * 实习生选题统计结果
 *
 * @param totalCount       实习生总数
 * @param selectedCount    已选题学生数量
 * @param completionRate   完成率（整数百分比，如 80 表示 80%）
 * @param uncompletedCount 未完成学生数量
 */
public record InternSelectionStatisticsResult(
        long totalCount,
        long selectedCount,
        int completionRate,
        long uncompletedCount
) {
}
