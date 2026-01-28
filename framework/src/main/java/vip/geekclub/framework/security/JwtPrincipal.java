package vip.geekclub.framework.security;

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
public record JwtPrincipal(String authId, String userType) implements Principal {

    private static final LazyUtils<JwtUtil> jwtUtil = new LazyUtils<>(() -> ApplicationUtil.getBean(JwtUtil.class));

    // JWT声明常量
    private static final String SUBJECT_CLAIM = "sub";
    private static final String USER_TYPE_CLAIM = "userType";

    public JwtPrincipal {
        AssertUtil.notBlank(authId, ()->"用户ID不能为空");
        AssertUtil.notNull(userType, "用户类型不能为空");
    }

    @Override
    public String getName() {
        return "" ;
    }

    /**
     * 获取Map集合，用于JWT令牌生成
     */
    public Map<String, Object> getMap() {
        Map<String, Object> jwtMap = new HashMap<>();
        jwtMap.put(SUBJECT_CLAIM, authId);
        jwtMap.put(USER_TYPE_CLAIM, userType);
        return jwtMap;
    }

    /**
     * 获取JWT令牌（使用默认过期时间）
     * 
     * @return JWT令牌字符串
     */
    public String getToken() {
        return jwtUtil.getValue().generateToken(getMap());
    }
    
    /**
     * 获取JWT令牌（指定过期时间）
     * 
     * @param expirationSeconds 过期时间（秒）
     * @return JWT令牌字符串
     */
    public String getToken(long expirationSeconds) {
        return jwtUtil.getValue().generateToken(getMap(), expirationSeconds);
    }

    /**
     * 从JWT解析结果构建JwtPrincipal
     */
    public static JwtPrincipal buildByMap(Map<String, Object> claims) {

        AssertUtil.notNull(claims, "JWT解析结果不能为空");

        Object subClaim = claims.get(SUBJECT_CLAIM);
        Object userTypeClaim = claims.get(USER_TYPE_CLAIM);

        AssertUtil.notNull(subClaim, "JWT声明缺少必要信息");
        AssertUtil.notNull(userTypeClaim, "JWT声明缺少必要信息");

        try {
            String authId = subClaim.toString();
            String userType = userTypeClaim.toString();
            return new JwtPrincipal(authId, userType);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("用户ID格式错误: " + subClaim, e);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("用户类型格式错误: " + userTypeClaim, e);
        }
    }

    /**
     * 从JWT令牌字符串解析JwtPrincipal
     */
    public static JwtPrincipal buildByToken(String token) {
        Map<String, Object> claims = jwtUtil.getValue().parseToken(token);
        return buildByMap(claims);
    }
}
