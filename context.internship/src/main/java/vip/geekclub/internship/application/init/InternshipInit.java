package vip.geekclub.internship.application.init;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.framework.initialize.InitTask;
import vip.geekclub.internship.domain.model.Intern;
import vip.geekclub.internship.domain.model.Thesis;
import vip.geekclub.internship.domain.repository.InternRepository;
import vip.geekclub.internship.domain.repository.ThesisRepository;
import vip.geekclub.security.application.command.principal.CreatePrincipalCommand;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.List;

@Component
@AllArgsConstructor
public class InternshipInit implements InitTask {

    private final InternRepository internRepository;
    private final ThesisRepository thesisRepository;
    private final CommandBus commandBus;


    @Override
    public void initialize() {
        // 初始化5个实习生
        String advisorName = "张莺";
        String className = "软件工程2301班";


        // 判断没有学生时才初始化
        if (internRepository.count() == 0) {
            Intern intern1 = new Intern("张三", "20230101001", className, advisorName);
            Intern intern2 = new Intern("李四", "20230101002", className, advisorName);
            Intern intern3 = new Intern("王五", "20230101003", className, advisorName);
            Intern intern4 = new Intern("赵六", "20230101004", className, advisorName);
            Intern intern5 = new Intern("孙七", "20230101005", className, advisorName);
            List<Intern> savedInterns = internRepository.saveAll(List.of(intern1, intern2, intern3, intern4, intern5));

            // 为每个实习生创建 Principal（密码默认为 666666）
            for (Intern intern : savedInterns) {
                CreatePrincipalCommand command = new CreatePrincipalCommand(
                        "STUDENT", intern.getAuthId(),
                        List.of(new IdentifierValue("STUDENT_NO",intern.getStudentNo())),
                        "666666"
                );
                commandBus.dispatch(command);
            }
        }

        // 判断没有论文时才初始化
        if (thesisRepository.count() == 0) {
            Thesis thesis1 = new Thesis("基于Spring Boot的微服务架构设计与实现", 5);
            Thesis thesis2 = new Thesis("基于人工智能的图像识别系统研究", 3);
            thesisRepository.saveAll(List.of(thesis1, thesis2));
        }


    }
}
