package vip.geekclub.manager.application.command.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.contract.value.SortOrder;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.manager.domain.model.Department;
import vip.geekclub.manager.domain.repository.DepartmentRepository;

/**
 * 创建部门命令处理器
 */
@RequiredArgsConstructor
@Service
public class CreateDepartmentCommandHandler implements CommandHandler<CreateDepartmentCommand, Long> {

    private final DepartmentRepository departmentRepository;

    @Override
    @Transactional
    public Long execute(CreateDepartmentCommand command) {

        // 1. 校验名称在同一父部门下不重复
        validateDepartmentNameUnique(command.name().trim(), command.parentId());

        // 2. 获取父部门
        Department parentDepartment = departmentRepository.findById(command.parentId())
                .orElseThrow(() -> new NotFoundException("指定的父部门不存在"));

        // 3. 创建子部门
        Department department = Department.createDepartment(command.name(), parentDepartment, SortOrder.of(command.sortOrder()), command.description());

        // 4. 保存部门
        departmentRepository.save(department);
        return department.getId();
    }

    /**
     * 校验 部门名称 在同一父部门下不重复
     */
    private void validateDepartmentNameUnique(String name, Long parentId) {
        if (departmentRepository.existsByNameAndParentId(name, parentId)) {
            throw new BusinessException("同一父部门下已存在相同名称的部门");
        }
    }

}