package vip.geekclub.internship.application.query.dto;

import vip.geekclub.framework.jooq.PageQuery;

/**
 * 论文选择结果列表查询参数
 */
public record ThesisSelectionListQuery(
        /*
         * 班级名称（精确查询）
         */
        String className,

        /*
         * 指导老师姓名（精确查询）
         */
        String advisorName,

        /*
         * 学生姓名（模糊查询）
         */
        String studentName,

        /*
         * 分页参数
         */
        PageQuery pageQuery
) {
    public ThesisSelectionListQuery {
        // 空值处理，将空字符串转为null
        if (className != null && className.isBlank()) {
            className = null;
        }
        if (advisorName != null && advisorName.isBlank()) {
            advisorName = null;
        }
        if (studentName != null && studentName.isBlank()) {
            studentName = null;
        }
        pageQuery = pageQuery != null ? pageQuery : new PageQuery(1, 10);
    }
}
