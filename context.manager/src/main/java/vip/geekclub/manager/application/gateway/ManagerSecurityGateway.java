package vip.geekclub.manager.application.gateway;

import vip.geekclub.security.domain.authentication.value.IdentifierValue;

import java.util.List;

/**
 * 安全模块防腐层接口
 * 用于调用 security 模块的服务
 */
public interface ManagerSecurityGateway {

    /**
     * 创建管理员凭证
     *
     * @param authId     认证标识
     * @param username   用户名
     * @param password   密码
     */
    void initAdmin(String authId, String username,String password);

    /**
     * 创建教师凭证
     *
     * @param authId      认证标识
     * @param identifiers 标识列表
     * @param password    密码
     */
    void createTeacherPrincipal(String authId, List<IdentifierValue> identifiers,
                                String password);

}
