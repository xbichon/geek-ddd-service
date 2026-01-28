package vip.geekclub.manager.application.init;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
//import vip.geekclub.manager.application.port.SecurityServicePort;
import vip.geekclub.manager.application.port.dto.TeacherCredential;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.TeacherRepository;
import vip.geekclub.security.domain.value.CredentialType;

import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class InitAdminRunner implements CommandLineRunner {
    private final TeacherRepository teacherRepository;
//    private final SecurityServicePort securityServicePort;

    @Async
    @Override
    public void run(String[] args) {

//        if (teacherRepository.existsByEmail("admin@example.com")) {
//            return;
//        }
//
//        log.info("初始化管理员用户...");
//        Teacher teacher = Teacher.createTeacher(
//                "管理员",
//                "18800000000",
//                "admin@example.com",
//                0L,
//                ""
//        );
//        teacherRepository.save(teacher);
//
//        // 2. 创建用户的凭证
//        securityServicePort.createCredential(new TeacherCredential(teacher.getAuthId()
//                        , "admin"
//                        , "888888"
//                        , CredentialType.USERNAME
//                        , Set.of(-1L)
//                )
//        );

    }
}
