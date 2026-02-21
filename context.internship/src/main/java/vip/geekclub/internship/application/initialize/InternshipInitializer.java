package vip.geekclub.internship.application.initialize;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.contract.UserType;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.framework.initialize.Initializer;
import vip.geekclub.internship.domain.model.Intern;
import vip.geekclub.internship.domain.model.Thesis;
import vip.geekclub.internship.domain.repository.InternRepository;
import vip.geekclub.internship.domain.repository.ThesisRepository;
import vip.geekclub.internship.domain.value.AchievementType;
import vip.geekclub.security.application.command.principal.CreatePrincipalCommand;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.List;

@Component
@AllArgsConstructor
public class InternshipInitializer implements Initializer {

    private final InternRepository internRepository;
    private final ThesisRepository thesisRepository;
    private final CommandBus commandBus;

    @Override
    public void initialize() {
        // 初始化5个实习生
        final String advisorName = "张莺";
        final String className = "软件工程2301班";


        List<Intern> allByAuthIdIsNull = internRepository.findAllByAuthIdIsNull();
        for (Intern intern : allByAuthIdIsNull) {
            intern.initAuthId();

            // 为每个实习生创建 Principal（密码默认为 666666）
            CreatePrincipalCommand command = new CreatePrincipalCommand(UserType.STUDENT, intern.getAuthId(),
                    List.of(new IdentifierValue("STUDENT_NO", intern.getStudentNo())),
                    "666666"
            );
            commandBus.dispatch(command);
        }
        internRepository.saveAll(allByAuthIdIsNull);

        // 判断没有论文时才初始化
        if (thesisRepository.count() == 0) {
            // 根据选题方向和成果形式初始化论文
            List<Thesis> theses = List.of(
                    createThesis("软件设计", "报告或作品"),
                    createThesis("软件开发", "报告或作品"),
                    createThesis("软件测试", "报告"),
                    createThesis("软件运维", "报告"),
                    createThesis("网络构建", "报告"),
                    createThesis("云计算技术应用", "报告"),
                    createThesis("大数据平台搭建", "报告"),
                    createThesis("数据库管理", "报告"),
                    createThesis("自媒体运营", "报告或作品"),
                    createThesis("计算机相关应用", "报告"),
                    createThesis("高可靠性园区网", "报告"),
                    createThesis("广域网搭建", "报告"),
                    createThesis("软件售前、售后", "报告或产品介绍文档、产品使用文档"),
                    createThesis("Android手机应用开发", "报告或作品"),
                    createThesis("网络安全", "报告"),
                    createThesis("人工智能应用", "报告")
            );
            thesisRepository.saveAll(theses);
        }
    }

    /**
     * 创建论文
     *
     * @param title       论文标题（选题方向）
     * @param achievement 成果形式（可能包含多个，用"或"分隔）
     * @return Thesis对象
     */
    private Thesis createThesis(String title, String achievement) {
        // 默认可选上限为5人
        Thesis thesis = new Thesis(title, 50);

        // 解析成果形式
        if (achievement.contains("或")) {
            String[] parts = achievement.split("或");
            for (String part : parts) {
                String cleanedPart = part.trim();
                if (!cleanedPart.isEmpty()) {
                    thesis.getAchievementTypes().add(new AchievementType(cleanedPart));
                }
            }
        } else {
            thesis.getAchievementTypes().add(new AchievementType(achievement.trim()));
        }

        return thesis;
    }
}
