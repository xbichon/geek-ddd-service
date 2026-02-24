package vip.geekclub.manager.application.gateway;

/**
 * 安全模块防腐层接口
 * 用于调用 security 模块的服务
 */
public interface SecurityGateway {

    /**
     * 创建管理员凭证
     *
     * @param authId     认证标识
     * @param username   用户名
     * @param password   密码
     */
    void createAdminPrincipal(String authId, String username,
                              String password);

}
