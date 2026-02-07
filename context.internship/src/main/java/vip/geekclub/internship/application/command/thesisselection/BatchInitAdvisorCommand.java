package vip.geekclub.internship.application.command.thesisselection;

import jakarta.validation.constraints.NotEmpty;
import vip.geekclub.framework.command.Command;

import java.util.List;

/**
 * 批量初始化指导教师命令
 */
public record BatchInitAdvisorCommand(

        @NotEmpty(message = "教师名字列表不能为空")
        List<String> advisorNames

) implements Command {

}