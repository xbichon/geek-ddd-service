package vip.geekclub.internship.application.query.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 论文选择结果列表查询结果
 * <p>同时用于 Excel 导出</p>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SelectionItemResult {

    @ExcelProperty("选题ID")
    private Long selectionId;

    @ExcelProperty("论文ID")
    private Long thesisId;

    @ExcelProperty("论文标题")
    private String thesisTitle;

    @ExcelProperty("成果形式")
    private String achievementType;

    @ExcelProperty("选择类型")
    private String selectionType;

    @ExcelProperty("学生姓名")
    private String studentName;

    @ExcelProperty("学号")
    private String studentNumber;

    @ExcelProperty("班级")
    private String className;

    @ExcelProperty("指导老师")
    private String advisorName;

    @ExcelProperty("组员")
    private String groupMembers;
}