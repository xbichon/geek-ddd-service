package vip.geekclub.manager.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.command.IdResult;
import vip.geekclub.manager.application.command.dto.CreateTeacherCommand;
import vip.geekclub.manager.application.port.SecurityServicePort;
import vip.geekclub.manager.application.port.dto.TeacherCredential;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import vip.geekclub.manager.domain.service.TeacherCreationUpdateValidator;
import vip.geekclub.security.domain.value.CredentialType;

import java.util.Set;

/**
 * 创建教师命令处理器
 */
@Transactional
@Service
@RequiredArgsConstructor
public class CreateTeacherCommandHandler implements CommandHandler<CreateTeacherCommand, IdResult> {

    private final TeacherRepository teacherRepository;
    private final TeacherCreationUpdateValidator teacherCreationUpdateValidator;
    private final SecurityServicePort securityServicePort;

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

        // 3. 创建用户的凭证
        securityServicePort.createCredential(new TeacherCredential(teacher.getAuthId()
                , teacher.getEmail()
                , "12345678"
                , CredentialType.EMAIL
                , Set.of()
        ));

        // 4. 返回结果
        return CommandResult.ok(teacher.getId());
    }
}