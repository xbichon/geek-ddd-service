package vip.geekclub.manager.command.dto;

import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Range;
import vip.geekclub.common.command.Command;
import vip.geekclub.manager.domain.Department;
import vip.geekclub.manager.common.SortOrder;

/**
 * 创建部门命令
 *
 * @author geekclub
 * @since 1.0
 */
public record CreateDepartmentCommand(

        @NotBlank(message = "部门名称不能为空")
        @Size(max = Department.maxNameLength, message = "部门名称长度不能超过50个字符")
        String name,

        @Range(min = SortOrder.MIN_VALUE, max = SortOrder.MAX_VALUE
                , message = "排序顺序必须在" + SortOrder.MIN_VALUE + "-" + SortOrder.MAX_VALUE + "之间")
        Integer sortOrder,

        @NotNull(message = "父级部门ID不能为空")
        Long parentId,

        @Size(max = Department.maxDescriptionLength, message = "描述长度不能超过200个字符")
        String description

) implements Command {
}