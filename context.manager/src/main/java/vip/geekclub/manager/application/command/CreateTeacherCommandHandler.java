package vip.geekclub.manager.application.command;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.manager.application.command.dto.CreateTeacherCommand;
import vip.geekclub.manager.application.gateway.ManagerSecurityGateway;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import vip.geekclub.manager.domain.service.TeacherCreationUpdateValidator;
import vip.geekclub.security.domain.authentication.value.IdentifierValue;

import java.util.List;

/**
 * 创建教师命令处理器
 */
@Transactional
@Service
@RequiredArgsConstructor
public class CreateTeacherCommandHandler implements CommandHandler<CreateTeacherCommand, Long> {

    private final TeacherRepository teacherRepository;
    private final TeacherCreationUpdateValidator teacherCreationUpdateValidator;
    private final ManagerSecurityGateway securityGateway;

    @Override
    public Long execute(CreateTeacherCommand command) {
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
        securityGateway.createTeacherPrincipal(
                teacher.getAuthId(),
                List.of(
                        new IdentifierValue(IdentifierValue.EMAIL, teacher.getEmail()),
                        new IdentifierValue(IdentifierValue.PHONE, teacher.getPhone())
                ),
                "123456"
        );

        // 4. 返回结果
        return teacher.getId();
    }
}