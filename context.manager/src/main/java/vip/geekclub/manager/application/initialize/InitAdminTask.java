package vip.geekclub.manager.application.initialize;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.command.CommandDispatcher;
import vip.geekclub.framework.initialize.InitTask;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.TeacherRepository;
import vip.geekclub.security.application.command.principal.CreateAdminCommand;

import java.beans.Transient;

@Slf4j
@Service
@AllArgsConstructor
public class InitAdminTask implements InitTask {
    private final TeacherRepository teacherRepository;

    @Transient
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
                "admin",
                "888888",
                teacher.getAuthId(),
                "teacher"
        );
        CommandDispatcher.dispatch(command);
    }
}
