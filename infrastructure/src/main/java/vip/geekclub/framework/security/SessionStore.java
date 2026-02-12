package vip.geekclub.framework.security;

public interface SessionStore {
    /**
     * 创建令牌
     *
     * @param authentication 认证信息
     * @return 令牌
     */
    String create(UserAuthentication authentication);

    /**
     * 加载令牌
     *
     * @param token 令牌
     * @return 认证信息
     */
    UserAuthentication load(String token);
}
