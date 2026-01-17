package vip.geekclub.manager.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.command.IdResult;
import vip.geekclub.common.exception.ValidationException;
import vip.geekclub.manager.command.dto.CreateDepartmentCommand;
import vip.geekclub.manager.common.SortOrder;
import vip.geekclub.manager.domain.Department;
import vip.geekclub.manager.domain.DepartmentRepository;

/**
 * 创建部门命令处理器
 */
@RequiredArgsConstructor
@Service
public class CreateDepartmentCommandHandler implements CommandHandler<CreateDepartmentCommand, IdResult> {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public CommandResult<IdResult> execute(CreateDepartmentCommand command) {

        // 1. 校验名称在同一父部门下不重复
        validateDepartmentNameUnique(command.name().trim(), command.parentId());

        // 2. 获取父部门
        Department parentDepartment = departmentRepository.findById(command.parentId())
                .orElseThrow(() -> new ValidationException("指定的父部门不存在"));

        // 3. 创建子部门
        Department department = Department.createDepartment(command.name(), parentDepartment, SortOrder.of(command.sortOrder()), command.description());

        // 4. 保存部门
        departmentRepository.save(department);
        return CommandResult.ok(department.getId());
    }

    /**
     * 校验 部门名称 在同一父部门下不重复
     */
    private void validateDepartmentNameUnique(String name, Long parentId) {
        if (departmentRepository.existsByNameAndParentId(name, parentId)) {
            throw new ValidationException("同一父部门下已存在相同名称的部门");
        }
    }

}