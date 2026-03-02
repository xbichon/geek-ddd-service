package vip.geekclub.manager.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.manager.application.command.dto.CreateTeacherCommand;
import vip.geekclub.manager.application.command.dto.UpdateTeacherCommand;
import vip.geekclub.manager.domain.model.Department;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.DepartmentRepository;
import vip.geekclub.manager.domain.repository.TeacherRepository;

/**
 * 教师业务规则验证器
 * 处理跨聚合的教师相关业务规则验证，包括唯一性验证和关联数据验证
 */
@Service
@RequiredArgsConstructor
public class TeacherCreationUpdateValidator {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;

    /**
     * 验证手机号唯一性
     */
    private void validatePhoneUnique(String phone) {
        if (StringUtils.hasText(phone) && teacherRepository.existsByPhone(phone)) {
            throw new BusinessException("已存在相同手机号的教师");
        }
    }

    /**
     * 验证邮箱唯一性
     */
    private void validateEmailUnique(String email) {
        if (StringUtils.hasText(email) && teacherRepository.existsByEmail(email)) {
            throw new BusinessException("已存在相同邮箱的教师");
        }
    }


    /**
     * 验证部门信息
     *
     * @param departmentId 部门ID
     */
    private void validateDepartment(Long departmentId) {
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new NotFoundException("指定的部门不存在"));

        if (department.isDisabled()) {
            throw new BusinessException("所属部门已禁用，不能操作教师");
        }
    }

    /**
     * 统一验证创建教师命令的完整性
     *
     * @param command 创建教师命令，包含所有必要信息
     */
    public void validateForCreate(CreateTeacherCommand command) {
        validatePhoneUnique(command.phone());
        validateEmailUnique(command.email());
        validateDepartment(command.departmentId());
    }

    /**
     * 统一验证更新教师命令的完整性
     *
     * @param command         更新教师命令，包含所有更新信息
     * @param existingTeacher 当前存在的教师对象，用于判断字段是否变化
     */
    public void validateForUpdate(UpdateTeacherCommand command, Teacher existingTeacher) {
        // 1. 手机号验证（如变化则验证）
        if (existingTeacher.isChangePhone(command.phone())) {
            validateEmailUnique(command.phone());
        }

        // 2. 邮箱验证（如变化则验证）
        if (existingTeacher.isChangeEmail(command.email())) {
            validatePhoneUnique(command.email());
        }

        // 3. 部门验证（总是验证）
        validateDepartment(command.departmentId());
    }
}