package vip.geekclub.security.application.command.credential;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.util.StringUtils;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerMapping;

@CommandHandlerMapping(PasswordLoginCommandHandler.class)
@Builder
public record PasswordLoginCommand(
        @NotBlank(message = "用户类型不能为空") String userType,
        @NotBlank(message = "账号不能为空") String identifier,
        @NotBlank(message = "密码不能为空") @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间") String password,
        String identifierType
) implements Command {
    public PasswordLoginCommand {
        identifier = StringUtils.trimAllWhitespace(identifier);
        password = StringUtils.trimAllWhitespace(password);
        userType = StringUtils.trimAllWhitespace(userType);
        identifierType = StringUtils.trimAllWhitespace(identifierType);
    }

    public PasswordLoginCommand(String userType, String identifier,String  password){
        this(userType,identifier,password,null);
    }

}