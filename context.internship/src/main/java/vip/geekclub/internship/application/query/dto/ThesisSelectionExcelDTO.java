package vip.geekclub.internship.application.query.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 论文选题Excel导出数据传输对象
 */
@Data
public class ThesisSelectionExcelDTO {

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

    /**
     * 从 ThesisSelectionListResult 转换为 Excel DTO
     */
    public static ThesisSelectionExcelDTO from(ThesisSelectionListResult result) {
        ThesisSelectionExcelDTO dto = new ThesisSelectionExcelDTO();
        dto.setSelectionId(result.selectionId());
        dto.setThesisId(result.thesisId());
        dto.setThesisTitle(result.thesisTitle());
        dto.setAchievementType(result.achievementType());
        dto.setSelectionType(result.selectionType());
        dto.setStudentName(result.studentName());
        dto.setStudentNumber(result.studentNumber());
        dto.setClassName(result.className());
        dto.setAdvisorName(result.advisorName());
        dto.setGroupMembers(result.groupMembers() != null ? result.groupMembers() : "");
        return dto;
    }
}