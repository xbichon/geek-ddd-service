package vip.geekclub.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.security.SessionStore;
import vip.geekclub.framework.security.UserAuthentication;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.support.jwt.JwtUtil;
import vip.geekclub.support.jwt.JwtValue;

/**
 * JWT主体信息
 * 包含用户ID和用户类型信息
 */
@Component
@RequiredArgsConstructor
public class SessionStoreImpl implements SessionStore {

    private final JwtUtil jwtUtil;
    private final static long expirationSeconds = 60 * 60 * 14 * 30;

    public String create(UserAuthentication authentication) {
        UserPrincipal userPrincipal = authentication.getUserPrincipal();
        return jwtUtil.generateToken(userPrincipal.authId(), userPrincipal, expirationSeconds);
    }

    public UserAuthentication load(String token) {
        JwtValue<UserPrincipal> jwtValue = jwtUtil.parseToken(token, UserPrincipal.class);
        return new UserAuthentication(jwtValue.data());
    }
}
