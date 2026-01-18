package vip.geekclub.education.students.command.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import vip.geekclub.framework.command.Command;
import vip.geekclub.education.students.common.Sex;
import vip.geekclub.education.students.domain.Student;

/**
 * 创建学生的命令
 */
public record CreateStudentCommand(
        @NotBlank(message = "学生姓名不能为空")
        @Max(value = Student.MAX_NAME_LENGTH, message = "学生姓名长度不能超过" + Student.MAX_NAME_LENGTH + "个字符")
        String name,
        Sex sex) implements Command {
}
