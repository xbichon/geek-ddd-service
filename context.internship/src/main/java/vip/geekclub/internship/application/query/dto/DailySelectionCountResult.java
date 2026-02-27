package vip.geekclub.internship.application.query.dto;

import java.time.LocalDate;

/**
 * 每日选题数量统计结果
 *
 * @param date          日期
 * @param selectionCount 选题数量
 */
public record DailySelectionCountResult(
        LocalDate date,
        Long selectionCount
) {
}
