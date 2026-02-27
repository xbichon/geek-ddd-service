package vip.geekclub.integration.gateway;

/**
 * 安全模块防腐层接口
 * 用于调用 security 模块的服务
 */
public interface IntegrationSecurityGateway {

    /**
     * 验证用户密码
     *
     * @param userType   用户类型
     * @param identifier 用户标识（用户名/邮箱/手机号）
     * @param password   密码
     * @return 认证标识 authId
     */
    String verifyPassword(String userType, String identifier, String password);
}
