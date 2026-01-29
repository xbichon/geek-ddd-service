package vip.geekclub.framework.security;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.framework.utils.JwtUtil;

import java.util.Map;
import java.util.Set;

/**
 * JWT主体信息
 * 包含用户ID和用户类型信息
 */
@Component
@RequiredArgsConstructor
public class AuthSessionManagerImpl implements AuthSessionManager {

    private final JwtUtil jwtUtil;

    // JWT声明常量
    private static final String SUBJECT_CLAIM = "sub";
    private static final String USER_TYPE_CLAIM = "userType";

    public String createSession(UserAuthenticationToken authentication) {
        UserPrincipal userPrincipal = authentication.getUserPrincipal();
        return jwtUtil.generateToken(Map.of(
                AuthSessionManagerImpl.SUBJECT_CLAIM, userPrincipal.authId(),
                AuthSessionManagerImpl.USER_TYPE_CLAIM, userPrincipal.userType()
        ), 60 * 60 * 14 * 30);
    }

    public UserAuthenticationToken getSession(String token) {
        Map<String, Object> claims = jwtUtil.parseToken(token);

        AssertUtil.notNull(claims, "JWT解析结果不能为空");

        Object subClaim = claims.get(AuthSessionManagerImpl.SUBJECT_CLAIM);
        Object userTypeClaim = claims.get(AuthSessionManagerImpl.USER_TYPE_CLAIM);

        AssertUtil.notNull(subClaim, "JWT声明缺少必要信息");
        AssertUtil.notNull(userTypeClaim, "JWT声明缺少必要信息");

        try {
            String authId = subClaim.toString();
            String userType = userTypeClaim.toString();
            UserPrincipal userPrincipal = new UserPrincipal(authId, userType);
            return new UserAuthenticationToken(userPrincipal, Set.of());

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("用户ID格式错误: " + subClaim, e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("用户类型格式错误: " + userTypeClaim, e);
        }
    }
}
