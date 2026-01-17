package vip.geekclub.manager.command.dto;

import jakarta.validation.constraints.*;
import vip.geekclub.common.command.Command;
import vip.geekclub.manager.domain.Teacher;
import vip.geekclub.manager.domain.TeacherStatus;

/**
 * 更新教师命令
 *
 * @author geekclub
 * @since 1.0
 */
public record UpdateTeacherCommand(

        @NotNull(message = "教师ID不能为空")
        Long id,

        @NotBlank(message = "教师姓名不能为空")
        @Size(max = Teacher.maxNameLength, message = "教师姓名长度不能超过50个字符")
        String name,

        @NotBlank(message = "手机号不能为空")
        @Size(max = Teacher.maxPhoneLength, message = "手机号长度不能超过20个字符")
        String phone,

        @Email(message = "邮箱格式不正确")
        @Size(max = Teacher.maxEmailLength, message = "邮箱长度不能超过100个字符")
        String email,

        @NotNull(message = "所属部门ID不能为空")
        Long departmentId,

        @NotNull(message = "教师状态不能为空")
        TeacherStatus status,

        @Size(max = Teacher.maxRemarkLength, message = "备注长度不能超过200个字符")
        String remark

) implements Command {
}