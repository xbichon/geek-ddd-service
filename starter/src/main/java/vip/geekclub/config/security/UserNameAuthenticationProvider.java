package vip.geekclub.config.security;

import lombok.NonNull;
import org.springframework.security.authentication.AuthenticationProvider;
import vip.geekclub.framework.command.CommandDispatcher;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.security.JwtAuthentication;
import vip.geekclub.framework.security.JwtPrincipal;
import vip.geekclub.security.application.command.credential.VerifyPasswordCommand;
import vip.geekclub.security.application.command.credential.VerifyPasswordResult;
import vip.geekclub.security.application.query.AuthenticationQueryService;
import vip.geekclub.security.application.query.dto.CredentialResult;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vip.geekclub.security.domain.value.CredentialType;
import vip.geekclub.security.domain.value.IdentifierType;


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

    private final PasswordEncoder passwordEncoder;
    private final AuthenticationQueryService authenticationQueryService;

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
            VerifyPasswordCommand command = new VerifyPasswordCommand(username, password);
            CommandResult<VerifyPasswordResult> commandResult = CommandDispatcher.dispatch(command);
            VerifyPasswordResult verifyPasswordResult = commandResult.data();

            JwtPrincipal jwtPrincipal = new JwtPrincipal(verifyPasswordResult.authId(), verifyPasswordResult.userType().toString());
            return new JwtAuthentication(jwtPrincipal);
        } catch (Exception e) {
            throw new BadCredentialsException(e.getMessage());
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}