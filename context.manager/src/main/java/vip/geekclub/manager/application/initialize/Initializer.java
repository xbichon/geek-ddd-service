package vip.geekclub.manager.application.initialize;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.manager.application.gateway.ManagerSecurityGateway;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.TeacherRepository;

@Slf4j
@Service
@AllArgsConstructor
public class Initializer implements vip.geekclub.framework.initialize.Initializer {
    private final TeacherRepository teacherRepository;
    private final ManagerSecurityGateway securityGateway;

    @Transactional
    @Override
    public void initialize() {
        if (teacherRepository.count() > 0) {
            return;
        }

        log.info("初始化系统管理员角色...");
        securityGateway.initializeSystemAdminRole();

        log.info("初始化管理员用户...");
        Teacher teacher = Teacher.createTeacher(
                "管理员",
                "",
                "",
                0L,
                ""
        );
        teacherRepository.save(teacher);

        // 2. 创建用户的凭证
        securityGateway.createAdminPrincipal(
                teacher.getAuthId(),
                "admin",
                "888888"
        );
    }
}
