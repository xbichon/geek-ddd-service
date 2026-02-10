package vip.geekclub.internship.domain.value;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vip.geekclub.framework.utils.StringUtil;

import java.util.List;

/**
 * 结组申请DTO
 * 用于创建选题命令中的结组申请信息
 */
public record TeamApplicationValue(

        @NotBlank(message = "结组原因不能为空")
        @Size(max = 100, message = "结组原因长度不能超过100")
        String reason,

        @NotNull(message = "小组成员集合不能为空")
        @Valid
        List<TeamMemberValue> members
) {

    public TeamApplicationValue {
        reason = StringUtil.trimToNull(reason);

        if(members == null || members.isEmpty()) {
            throw new IllegalArgumentException("结组申请必须包含至少两个小组成员");
        }
        if (members.size() < 2 || members.size() > 5) {
            throw new IllegalArgumentException("结组申请成员数量必须在2-5人之间");
        }
    }
}