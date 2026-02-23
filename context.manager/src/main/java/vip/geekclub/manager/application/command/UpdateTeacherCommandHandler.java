package vip.geekclub.manager.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.manager.application.command.dto.UpdateTeacherCommand;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.TeacherRepository;
import vip.geekclub.manager.domain.service.TeacherCreationUpdateValidator;

/**
 * 更新教师命令处理器
 */
@Transactional
@RequiredArgsConstructor
@Service
public class UpdateTeacherCommandHandler implements VoidCommandHandler<UpdateTeacherCommand> {

    private final TeacherRepository teacherRepository;
    private final TeacherCreationUpdateValidator teacherCreationUpdateValidator;

    @Override
    public void executeVoid(UpdateTeacherCommand command) {
        // 1. 获取教师
        Teacher teacher = teacherRepository.findById(command.id())
                .orElseThrow(() -> new ValidationException("指定的教师不存在"));

        // 2. 统一验证更新命令
        teacherCreationUpdateValidator.validateForUpdate(command, teacher);

        // 3. 更新教师信息 并保存
        teacher.updateTeacher(
                command.name(),
                command.phone(),
                command.email(),
                command.departmentId(),
                command.status(),
                command.remark()
        );
        teacherRepository.save(teacher);
    }

}