package vip.geekclub.internship.domain.value;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import vip.geekclub.framework.utils.StringUtil;

/**
 * 小组成员DTO
 * 用于创建选题命令中的成员信息
 */
public record TeamMemberValue(

        @NotNull(message = "学生ID不能为空")
        Long studentId,

        @NotNull(message = "职责描述不能为空")
        @Size(max = 40, message = "职责描述长度不能超过40")
        String responsibility
) {

    public TeamMemberValue {
        responsibility = StringUtil.trimToNull(responsibility);
    }
}