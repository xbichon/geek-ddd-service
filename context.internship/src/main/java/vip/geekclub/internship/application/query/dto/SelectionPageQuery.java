package vip.geekclub.internship.application.query.dto;

import vip.geekclub.support.jooq.PageQuery;

/**
 * 论文选择结果列表查询参数
 */
public record SelectionPageQuery(

        /*
         * 论文ID（精确查询）
         */
        Long thesisId,

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
         * 页码（从1开始）
         */
        Integer pageNum,

        /*
         * 每页大小
         */
        Integer pageSize

) implements PageQuery {
    public SelectionPageQuery {

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
    }
}
