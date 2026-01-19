package vip.geekclub.manager.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.command.IdResult;
import vip.geekclub.framework.domain.DomainEventPublisher;
import vip.geekclub.manager.application.command.dto.CreateTeacherCommand;
import vip.geekclub.manager.domain.event.UserCreatedEvent;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.TeacherRepository;
import vip.geekclub.manager.domain.service.TeacherCreationUpdateValidator;

/**
 * 创建教师命令处理器
 */
@Transactional
@RequiredArgsConstructor
@Service
public class CreateTeacherCommandHandler implements CommandHandler<CreateTeacherCommand, IdResult> {

    private final TeacherRepository teacherRepository;
    private final TeacherCreationUpdateValidator teacherCreationUpdateValidator;
    private final DomainEventPublisher domainEventPublisher;

    @Override
    public CommandResult<IdResult> execute(CreateTeacherCommand command) {
        // 1. 验证参数
        teacherCreationUpdateValidator.validateForCreate(command);

        // 2. 创建教师并保存
        Teacher teacher = Teacher.createTeacher(
                command.name(),
                command.phone(),
                command.email(),
                command.departmentId(),
                command.remark()
        );
        teacherRepository.save(teacher);
        DomainEventPublisher.getInstance().get().publishEvent(new UserCreatedEvent(
                teacher.getId(),
                teacher.getEmail(),
                teacher.getPhone(),
                teacher.getExternalUuid()
        ));

        // 3. 返回结果
        return CommandResult.ok(teacher.getId());
    }
}