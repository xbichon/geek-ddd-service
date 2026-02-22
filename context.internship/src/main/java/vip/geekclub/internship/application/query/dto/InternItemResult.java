package vip.geekclub.internship.application.query.dto;

/**
 * 实习生列表查询结果
 */
public record InternItemResult(
        Long id,
        String name,
        String studentNo,
        String className,
        String advisorName,
        Boolean selected
) {
}
