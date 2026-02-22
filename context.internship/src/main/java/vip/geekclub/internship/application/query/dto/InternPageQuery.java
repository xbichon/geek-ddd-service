package vip.geekclub.internship.application.query.dto;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import org.springframework.util.StringUtils;
import vip.geekclub.framework.jooq.PageParam;

/**
 * 实习生分页查询参数
 */
public record InternPageQuery(

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
         * 学号（精确查询）
         */
        String studentNo,

        /*
         * 是否已选题（true-已选题，false-未选题）
         */
        Boolean selected,

        /*
         * 分页参数
         */
        @JsonUnwrapped
        PageParam pageParam

) {
    public InternPageQuery {

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
        if (studentNo != null && studentNo.isBlank()) {
            studentNo = null;
        }
    }
}
