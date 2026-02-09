package vip.geekclub.internship.application.init;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.framework.initialize.InitTask;

import java.util.List;

@Component
@AllArgsConstructor
public class InternshipInit implements InitTask {

    private final CommandBus commandBus;

    @Override
    public void initialize() {
        // 初始化指导教师
        List<String> advisorNames = List.of(
                "张莺", "李慧", "史红生", "崔红伟", "田东方",
                "马鹏程", "温馨", "田晓霞", "卢华燕", "王琦",
                "谷壬倩", "孟祥佳", "杜敬一", "岳少涛", "刘明凯",
                "何飞", "王甜", "李笑雨", "张恩琪", "张晓楠"
        );

//        BatchInitAdvisorCommand command = new BatchInitAdvisorCommand(advisorNames);
//        commandBus.dispatch(command);
    }
}
