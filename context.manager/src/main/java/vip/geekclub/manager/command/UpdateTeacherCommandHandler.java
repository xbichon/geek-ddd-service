package vip.geekclub.manager.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.exception.ValidationException;
import vip.geekclub.manager.command.dto.UpdateTeacherCommand;
import vip.geekclub.manager.domain.Department;
import vip.geekclub.manager.domain.DepartmentRepository;
import vip.geekclub.manager.domain.Teacher;
import vip.geekclub.manager.domain.TeacherRepository;

/**
 * 更新教师命令处理器
 */
@Transactional
@RequiredArgsConstructor
@Service
public class UpdateTeacherCommandHandler implements CommandHandler<UpdateTeacherCommand, Void> {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;


    @Override
    public CommandResult<Void> execute(UpdateTeacherCommand command) {
        // 1. 获取教师
        Teacher teacher = teacherRepository.findById(command.id())
                .orElseThrow(() -> new ValidationException("指定的教师不存在"));

        // 2. 如果姓名变化，校验姓名不重复
        if (teacher.isChangeName(command.name().trim())) {
            validateTeacherNameUnique(command.name().trim(), command.id());
        }

        // 3. 如果手机号变化，校验手机号不重复
        if (!teacher.getPhone().equals(command.phone().trim())) {
            validateTeacherPhoneUnique(command.phone().trim(), command.id());
        }

        // 4. 如果邮箱变化，校验邮箱不重复
        if (command.email() != null && !command.email().trim().isEmpty() &&
                !command.email().trim().equals(teacher.getEmail())) {
            validateTeacherEmailUnique(command.email().trim(), command.id());
        }

        // 5. 获取部门信息
        Department department = departmentRepository.findById(command.departmentId())
                .orElseThrow(() -> new ValidationException("指定的部门不存在"));

        // 6. 验证部门状态
        if (department.isDisabled()) {
            throw new ValidationException("所属部门已禁用，不能更新教师");
        }

        // 7. 更新教师信息
        teacher.updateTeacher(
                command.name(),
                command.phone(),
                command.email(),
                command.departmentId(),
                command.status(),
                command.remark()
        );

        // 8. 保存教师
        teacherRepository.save(teacher);

        return CommandResult.ok();
    }

    /**
     * 校验教师姓名不重复（排除当前教师）
     */
    private void validateTeacherNameUnique(String name, Long excludeId) {
        teacherRepository.findAll().stream()
                .filter(t -> t.getName().equals(name) && !t.getId().equals(excludeId))
                .findFirst()
                .ifPresent(t -> {
                    throw new ValidationException("已存在相同姓名的教师");
                });
    }

    /**
     * 校验教师手机号不重复（排除当前教师）
     */
    private void validateTeacherPhoneUnique(String phone, Long excludeId) {
        teacherRepository.findAll().stream()
                .filter(t -> t.getPhone().equals(phone) && !t.getId().equals(excludeId))
                .findFirst()
                .ifPresent(t -> {
                    throw new ValidationException("已存在相同手机号的教师");
                });
    }

    /**
     * 校验教师邮箱不重复（排除当前教师）
     */
    private void validateTeacherEmailUnique(String email, Long excludeId) {
        teacherRepository.findAll().stream()
                .filter(t -> t.getEmail() != null && t.getEmail().equals(email) && !t.getId().equals(excludeId))
                .findFirst()
                .ifPresent(t -> {
                    throw new ValidationException("已存在相同邮箱的教师");
                });
    }
}