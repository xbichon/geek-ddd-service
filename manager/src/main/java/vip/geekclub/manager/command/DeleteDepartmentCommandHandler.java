package vip.geekclub.manager.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.VoidCommandHandler;
import vip.geekclub.common.exception.NotFoundException;
import vip.geekclub.common.exception.ValidationException;
import vip.geekclub.manager.command.dto.DeleteDepartmentCommand;
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
public class DeleteDepartmentCommandHandler implements VoidCommandHandler<DeleteDepartmentCommand> {

    private final DepartmentRepository departmentRepository;

    @Transactional
    @Override
    public void process(DeleteDepartmentCommand command) {
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
    }
}