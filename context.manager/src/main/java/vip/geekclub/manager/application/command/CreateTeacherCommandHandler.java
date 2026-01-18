package vip.geekclub.manager.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.command.IdResult;
import vip.geekclub.common.exception.ValidationException;
import vip.geekclub.manager.application.command.dto.CreateTeacherCommand;
import vip.geekclub.manager.domain.Department;
import vip.geekclub.manager.domain.DepartmentRepository;
import vip.geekclub.manager.domain.Teacher;
import vip.geekclub.manager.domain.TeacherRepository;

/**
 * 创建教师命令处理器
 */
@Transactional
@RequiredArgsConstructor
@Service
public class CreateTeacherCommandHandler implements CommandHandler<CreateTeacherCommand, IdResult> {

    private final TeacherRepository teacherRepository;
    private final DepartmentRepository departmentRepository;

    @Override
    public CommandResult<IdResult> execute(CreateTeacherCommand command) {
        // 1. 校验手机号不重复
        validateTeacherPhoneUnique(command.phone().trim());

        // 2. 校验邮箱不重复（如果提供了邮箱）
        if (command.email() != null && !command.email().trim().isEmpty()) {
            validateTeacherEmailUnique(command.email().trim());
        }

        // 3. 获取部门信息
        Department department = departmentRepository.findById(command.departmentId())
                .orElseThrow(() -> new ValidationException("指定的部门不存在"));

        // 4. 验证部门状态
        if (department.isDisabled()) {
            throw new ValidationException("所属部门已禁用，不能添加教师");
        }

        // 5. 创建教师
        Teacher teacher = Teacher.createTeacher(
                command.name(),
                command.phone(),
                command.email(),
                command.departmentId(),
                command.remark()
        );

        // 6. 保存教师
        teacherRepository.save(teacher);

        return CommandResult.ok(teacher.getId());
    }


    /**
     * 校验教师手机号不重复
     */
    private void validateTeacherPhoneUnique(String phone) {
        if (teacherRepository.existsByPhone(phone)) {
            throw new ValidationException("已存在相同手机号的教师");
        }
    }

    /**
     * 校验教师邮箱不重复
     */
    private void validateTeacherEmailUnique(String email) {
        if (teacherRepository.existsByEmail(email)) {
            throw new ValidationException("已存在相同邮箱的教师");
        }
    }

}