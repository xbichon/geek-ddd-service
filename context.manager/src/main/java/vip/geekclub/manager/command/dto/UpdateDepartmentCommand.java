package vip.geekclub.manager.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vip.geekclub.common.command.Command;
import vip.geekclub.manager.domain.DepartmentStatus;
import vip.geekclub.manager.domain.Department;
import vip.geekclub.manager.domain.SortOrder;

/**
 * 更新部门命令
 *
 * @author geekclub
 * @since 1.0
 */
public record UpdateDepartmentCommand(
    @NotNull(message = "部门ID不能为空")
    Long id,

    @NotBlank(message = "部门名称不能为空")
    @Size(max = Department.maxNameLength, message = "部门名称长度不能超过50个字符")
    String name,

    @NotNull(message = "排序顺序不能为空")
    SortOrder sortOrder,

    DepartmentStatus status,

    @Size(max = Department.maxDescriptionLength, message = "描述长度不能超过200个字符")
    String description

) implements Command {
}