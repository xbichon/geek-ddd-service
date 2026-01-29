package vip.geekclub.framework.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.utils.JwtUtil;
import vip.geekclub.framework.utils.JwtValue;

import java.util.Set;

/**
 * JWT主体信息
 * 包含用户ID和用户类型信息
 */
@Component
@RequiredArgsConstructor
public class AuthSessionManagerImpl implements AuthSessionManager {

    private final JwtUtil jwtUtil;
    private final static long expirationSeconds = 60 * 60 * 14 * 30;

    public String createSession(UserAuthenticationToken authentication) {
        UserPrincipal userPrincipal = authentication.getUserPrincipal();
        return jwtUtil.generateToken(userPrincipal.authId(), userPrincipal, expirationSeconds);
    }

    public UserAuthenticationToken getSession(String token) {
        JwtValue<UserPrincipal> jwtValue = jwtUtil.parseToken(token, UserPrincipal.class);
        return new UserAuthenticationToken(jwtValue.data(), Set.of());
    }
}
