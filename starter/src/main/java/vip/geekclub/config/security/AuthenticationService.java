package vip.geekclub.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
 * 认证门面服务：对外提供业务友好的认证接口，隐藏 AuthenticationManager 和具体 Token 的细节。
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    /**
     * 用户名密码认证
     */
    public UserSession authenticateUsernamePassword(String username, String password) {
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return (UserSession) authenticationManager.authenticate(authRequest);
    }

    /**
     * 微信小程序认证
     *
     * @param unionId 微信用户唯一标识
     * @return 认证后的用户会话
     */
    public UserSession authenticateWechatUnionId(String unionId) {
        WechatAuthenticationToken authRequest = new WechatAuthenticationToken(unionId);
        return (UserSession) authenticationManager.authenticate(authRequest);
    }
}