package vip.geekclub.internship.application.query.dto;

/**
 * 选择者信息查询结果
 */
public record SelectorResult(
        /*
         * 学生ID
         */
        Long studentId,

        /*
         * 学生姓名
         */
        String studentName,

        /*
         * 学生学号
         */
        String studentNo,

        /*
         * 班级名称
         */
        String className
) {
}