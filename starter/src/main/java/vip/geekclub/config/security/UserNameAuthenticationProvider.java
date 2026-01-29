package vip.geekclub.config.security;

import lombok.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import vip.geekclub.framework.command.CommandDispatcher;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.security.UserAuthenticationToken;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.security.application.command.credential.PasswordLoginCommand;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Set;


/**
 * 登录验证器
 * 实现 AuthenticationProvider 接口，用于提供自定义的身份验证逻辑。
 * 在 authenticate 方法中，根据提供的用户名和密码进行验证，并返回一个包含用户信息和权限的 Authentication 对象。
 *
 * @author leo
 */
@Service
@AllArgsConstructor
public class UserNameAuthenticationProvider implements AuthenticationProvider {


    @Override
    public Authentication authenticate(@NonNull Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;

        // 检查用户名和密码是否为空
        if (token.getPrincipal() == null || token.getCredentials() == null) {
            throw new BadCredentialsException("用户名或密码不能为空");
        }
        String username = token.getPrincipal().toString();
        String password = token.getCredentials().toString();

        try {
            PasswordLoginCommand command = new PasswordLoginCommand(username, password);
            CommandResult<UserPrincipal> commandResult = CommandDispatcher.dispatch(command);
            UserPrincipal userPrincipal = commandResult.data();
            return new UserAuthenticationToken(userPrincipal, Set.of());
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}