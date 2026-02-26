package vip.geekclub.internship.application.initialize;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.initialize.Initializer;
import vip.geekclub.internship.application.gateway.InternshipSecurityGateway;
import vip.geekclub.internship.domain.model.Intern;
import vip.geekclub.internship.domain.model.Thesis;
import vip.geekclub.internship.domain.repository.InternRepository;
import vip.geekclub.internship.domain.repository.ThesisRepository;
import vip.geekclub.internship.domain.value.AchievementType;

import java.util.List;

@Component
@AllArgsConstructor
public class InternshipInitializer implements Initializer {

    private final InternRepository internRepository;
    private final ThesisRepository thesisRepository;
    private final InternshipSecurityGateway securityGateway;

    @Override
    public void initialize() {
        List<Intern> allByAuthIdIsNull = internRepository.findAllByAuthIdIsNull();
        for (Intern intern : allByAuthIdIsNull) {
            intern.initAuthId();

            // 根据指导老师手机号设置初始密码（使用完整手机号）
            String password = switch (intern.getAdvisorName()) {
                case "张莺" -> "33807292";     // 张莺
                case "田东方" -> "30121290";    // 田东方
                case "孟祥佳" -> "30666028";    // 孟祥佳
                case "许昭霞" -> "13312903";    // 许昭霞
                case "田晓霞" -> "31142317";    // 田晓霞
                case "王甜" -> "36009727";      // 王甜
                case "温馨" -> "33777128";      // 温馨
                case "岳少涛" -> "32122570";    // 岳少涛
                case "何飞" -> "21889813";      // 何飞
                case "孙晓昂" -> "03294810";    // 孙晓昂
                case "刘明凯" -> "69166760";    // 刘明凯
                case "卢华燕" -> "03118762";    // 卢华燕
                case "杨怡辰" -> "11716951";    // 杨怡辰
                case "李慧" -> "81099610";      // 李慧
                case "崔红伟" -> "32128410";    // 崔红伟
                case "杜敬一" -> "31115330";    // 杜敬一
                case "王琦" -> "31149358";      // 王琦
                case "李笑雨" -> "30187925";    // 李笑雨
                case "张恩琪" -> "33115570";    // 张恩琪
                case "马鹏程" -> "11572820";    // 马鹏程
                case "史红生" -> "31933601";    // 史红生
                case "石明月" -> "31915602";    // 石明月
                case "张晓楠" -> "31089307";    // 张晓楠
                case "谷壬倩" -> "31828469";    // 谷壬倩
                case "刘爽" -> "33113680";      // 刘爽
                default -> "666666"; // 默认密码
            };

            // 统一调用网关创建学生凭证（使用完整手机号作为密码）
            securityGateway.createStudentPrincipal(
                    intern.getAuthId(),
                    intern.getStudentNo(),
                    password
            );

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
