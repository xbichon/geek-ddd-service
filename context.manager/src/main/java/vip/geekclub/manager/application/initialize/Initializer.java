package vip.geekclub.manager.application.initialize;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandDispatcher;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.TeacherRepository;
import vip.geekclub.security.application.command.principal.CreateAdminCommand;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class Initializer implements vip.geekclub.framework.initialize.Initializer {
    private final TeacherRepository teacherRepository;

    @Transactional
    @Override
    public void initialize() {
        if (teacherRepository.existsByEmail("admin@example.com")) {
            return;
        }

        log.info("初始化管理员用户...");
        Teacher teacher = Teacher.createTeacher(
                "管理员",
                "18800000000",
                "admin@example.com",
                0L,
                ""
        );
        teacherRepository.save(teacher);

        // 2. 创建用户的凭证
        CreateAdminCommand command = new CreateAdminCommand(
                List.of(IdentifierValue.ofUsername("admin"), IdentifierValue.ofEmail("admin@example.com")),
                "888888",
                teacher.getAuthId(),
                "teacher"
        );
        CommandDispatcher.dispatch(command);
    }
}
