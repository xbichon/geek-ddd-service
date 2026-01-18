package vip.geekclub.manager.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.exception.NotFoundException;
import vip.geekclub.common.exception.ValidationException;
import vip.geekclub.manager.application.command.dto.UpdateDepartmentCommand;
import vip.geekclub.manager.domain.Department;
import vip.geekclub.manager.domain.DepartmentRepository;

/**
 * 更新部门命令处理器
 *
 * @author geekclub
 * @since 1.0
 */
@AllArgsConstructor
@Service
public class UpdateDepartmentCommandHandler implements CommandHandler<UpdateDepartmentCommand, Void> {

    private final DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public CommandResult<Void> execute(UpdateDepartmentCommand command) {
        // 1. 获取部门
        Department department = departmentRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("部门不存在"));

        // 2. 判断是否更新部门名称
        if (department.isChangeName(command.name())) {
            // 2.1. 验证部门的名称在同一父部门下不重复
            if (departmentRepository.existsByNameAndParentId(command.name().trim(), department.getParentId())) {
                throw new ValidationException("同一父部门下已存在相同名称的部门");
            }
        }

        // 3. 更新部门信息
        department.updateDepartment(
                command.name(),
                command.sortOrder(),
                command.status(),
                command.description()
        );

        // 4. 保存部门
        departmentRepository.save(department);

        // 5. 返回成功结果
        return CommandResult.ok();
    }
}