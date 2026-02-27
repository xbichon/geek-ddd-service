package vip.geekclub.integration.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.security.application.command.credential.PasswordVerificationCommand;
import vip.geekclub.security.application.query.PermissionQueryService;

import java.util.Set;

/**
 * 安全模块门面
 * 封装对 security 模块的调用
 */
@Service
@RequiredArgsConstructor
public class SecurityFacade {

    private final CommandBus commandBus;
    private final PermissionQueryService permissionQueryService;

    /**
     * 验证用户密码
     *
     * @param userType   用户类型
     * @param identifier 用户标识（用户名/邮箱/手机号）
     * @param password   密码
     * @return 认证标识 authId
     */
    public String verifyPassword(String userType, String identifier, String password) {
        PasswordVerificationCommand command = new PasswordVerificationCommand(
                userType, identifier, password);
        return commandBus.dispatch(command);
    }

    /**
     * 根据认证标识获取用户权限列表
     *
     * @param authId 认证标识
     * @return 权限代码集合
     */
    public Set<String> getPermissionsByAuthId(String authId) {
        return permissionQueryService.getPermissionByAuthId(authId);
    }
}
