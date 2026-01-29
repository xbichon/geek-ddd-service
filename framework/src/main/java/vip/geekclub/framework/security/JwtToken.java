package vip.geekclub.framework.security;

import lombok.Getter;
import vip.geekclub.framework.utils.ApplicationUtil;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.framework.utils.JwtUtil;
import vip.geekclub.framework.utils.LazyUtils;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT主体信息
 * 包含用户ID和用户类型信息
 */
public class JwtToken {

    @Getter
    private final UserPrincipal userPrincipal;
    private static final JwtUtil jwtUtil = new JwtUtil();

    // JWT声明常量
    private static final String SUBJECT_CLAIM = "sub";
    private static final String USER_TYPE_CLAIM = "userType";

    public JwtToken(UserPrincipal userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

    /**
     * 获取JWT令牌（指定过期时间）
     *
     * @param expirationSeconds 过期时间（秒）
     * @return JWT令牌字符串
     */
    public String getToken(long expirationSeconds) {
        return jwtUtil.generateToken(Map.of(
                SUBJECT_CLAIM, userPrincipal.authId(),
                USER_TYPE_CLAIM, userPrincipal.userType()
        ), expirationSeconds);
    }

    public static JwtToken buildPrincipal(String token) {
        Map<String, Object> claims = jwtUtil.parseToken(token);

        AssertUtil.notNull(claims, "JWT解析结果不能为空");

        Object subClaim = claims.get(SUBJECT_CLAIM);
        Object userTypeClaim = claims.get(USER_TYPE_CLAIM);

        AssertUtil.notNull(subClaim, "JWT声明缺少必要信息");
        AssertUtil.notNull(userTypeClaim, "JWT声明缺少必要信息");

        try {
            String authId = subClaim.toString();
            String userType = userTypeClaim.toString();
            UserPrincipal userPrincipal = new UserPrincipal(authId, userType);
            return new JwtToken(userPrincipal);

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("用户ID格式错误: " + subClaim, e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("用户类型格式错误: " + userTypeClaim, e);
        }
    }
}
