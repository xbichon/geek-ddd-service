package vip.geekclub.manager.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.manager.application.command.dto.DeleteDepartmentCommand;
import vip.geekclub.manager.domain.Department;
import vip.geekclub.manager.domain.DepartmentRepository;

/**
 * 删除部门命令处理器
 *
 * @author geekclub
 * @since 1.0
 */
@AllArgsConstructor
@Service
public class DeleteDepartmentCommandHandler implements CommandHandler<DeleteDepartmentCommand, Void> {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(DeleteDepartmentCommand command) {

        // 1. 获取部门
        Department department = departmentRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("部门不存在"));

        // 2. 验证是否可以删除
        // 验证根部门不能删除
        department.validateDeletable();

        // 验证是否有子部门
        if (departmentRepository.existsByParentId(department.getId())) {
            throw new ValidationException("该部门下还有子部门，不能删除");
        }

        // 3. 删除部门
        departmentRepository.delete(department);

        // 4. 返回结果
        return CommandResult.ok("删除部门成功");
    }
}