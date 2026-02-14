package vip.geekclub.internship.application.query.dto;

import com.alibaba.excel.annotation.ExcelProperty;

/**
 * 论文选择结果列表查询结果
 * <p>同时用于 Excel 导出</p>
 */
public record ThesisSelectionListResult(
        @ExcelProperty("选题ID")
        Long selectionId,

        @ExcelProperty("论文ID")
        Long thesisId,

        @ExcelProperty("论文标题")
        String thesisTitle,

        @ExcelProperty("成果形式")
        String achievementType,

        @ExcelProperty("选择类型")
        String selectionType,

        @ExcelProperty("学生姓名")
        String studentName,

        @ExcelProperty("学号")
        String studentNumber,

        @ExcelProperty("班级")
        String className,

        @ExcelProperty("指导老师")
        String advisorName,

        @ExcelProperty("组员")
        String groupMembers
) {
}