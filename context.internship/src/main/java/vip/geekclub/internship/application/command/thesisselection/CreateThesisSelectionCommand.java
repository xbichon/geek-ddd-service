package vip.geekclub.internship.application.command.thesisselection;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import vip.geekclub.framework.command.Command;
import vip.geekclub.internship.domain.value.TeamApplicationValue;
import vip.geekclub.internship.domain.value.SelectionType;

/**
 * 创建选题命令
 */
public record CreateThesisSelectionCommand(

        @NotNull(message = "论文ID不能为空")
        Long thesisId,

        @NotNull(message = "成果形式不能为空")
        String achievementType,

        @NotNull(message = "选择者类型不能为空")
        SelectionType selectionType,

        @Valid
        TeamApplicationValue teamApplication
) implements Command {
    public CreateThesisSelectionCommand {
        if (selectionType == SelectionType.GROUP) {
            if (teamApplication == null) {
                throw new IllegalArgumentException("结组申请不能为空");
            }
        }
    }
}