package vip.geekclub.internship.application.command.thesisselection;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;
import vip.geekclub.contract.UserType;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.security.Authorize;
import vip.geekclub.internship.domain.value.SelectionType;
import vip.geekclub.internship.domain.value.TeamApplicationValue;

/**
 * 创建选题命令
 */
@Data
@Accessors(fluent = true)
public class CreateThesisSelectionCommand implements Command<Void> {

    @NotNull(message = "论文ID不能为空")
    private Long thesisId;

    @NotNull(message = "成果形式不能为空")
    private String achievementType;

    @NotNull(message = "选择者类型不能为空")
    private SelectionType selectionType;

    @JsonIgnore
    private Long creatorId;

    @Valid
    private TeamApplicationValue teamApplication;

    /**
     * 全参构造函数（包含验证逻辑）
     */
    public CreateThesisSelectionCommand(Long thesisId, String achievementType, SelectionType selectionType,
                                        Long creatorId, TeamApplicationValue teamApplication) {
        this.thesisId = thesisId;
        this.achievementType = achievementType;
        this.selectionType = selectionType;
        this.creatorId = creatorId;
        this.teamApplication = teamApplication;

        // 验证逻辑
        if (selectionType == SelectionType.GROUP) {
            if (teamApplication == null) {
                throw new IllegalArgumentException("结组申请不能为空");
            }
        }
    }

    public void setCreatorId(Long creatorId) {
        if (this.creatorId != null && this.creatorId != 0) {
            throw new IllegalArgumentException("创建者ID已经设置");
        }
        this.creatorId = creatorId;
    }
}
