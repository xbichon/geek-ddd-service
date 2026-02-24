package vip.geekclub.internship.application.gateway;

/**
 * 安全模块防腐层接口
 * 用于调用 security 模块的服务
 */
public interface InternshipSecurityGateway {

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
