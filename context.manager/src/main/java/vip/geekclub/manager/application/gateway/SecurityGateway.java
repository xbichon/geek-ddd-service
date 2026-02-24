package vip.geekclub.manager.application.gateway;

import java.util.List;

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
     * @param email      邮箱
     * @param password   密码
     * @param userType   用户类型
     */
    void createAdminPrincipal(String authId, String username,
                              String password);

    /**
     * 创建学生凭证
     *
     * @param authId     认证标识
     * @param studentNo  学号
     * @param password   密码
     */
    void createStudentPrincipal(String authId, String studentNo,
                                String password);
}
