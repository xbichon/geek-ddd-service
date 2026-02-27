package vip.geekclub.integration.gateway.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.integration.gateway.IntegrationSecurityGateway;
import vip.geekclub.security.application.command.credential.PasswordVerificationCommand;
import vip.geekclub.security.application.query.PermissionQueryService;

import java.util.Set;

/**
 * 安全模块防腐层实现
 */
@Component
@RequiredArgsConstructor
public class IntegrationSecurityGatewayImpl implements IntegrationSecurityGateway {

    private final CommandBus commandBus;
    private final PermissionQueryService permissionQueryService;

    @Override
    public String verifyPassword(String userType, String identifier, String password) {
        PasswordVerificationCommand command = new PasswordVerificationCommand(
                userType, identifier, password);
        return commandBus.dispatch(command);
    }

    @Override
    public Set<String> getPermissionsByAuthId(String authId) {
        return permissionQueryService.getPermissionByAuthId(authId);
    }
}
