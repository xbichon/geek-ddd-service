package vip.geekclub.manager.adapter.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.contract.UserType;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.manager.application.gateway.ManagerSecurityGateway;
import vip.geekclub.security.application.command.principal.CreateAdminCommand;
import vip.geekclub.security.application.command.principal.CreatePrincipalCommand;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.List;
import java.util.Set;

/**
 * 安全模块防腐层实现
 */
@Component
@RequiredArgsConstructor
public class ManagerSecurityGatewayImpl implements ManagerSecurityGateway {

    private final CommandBus commandBus;

    @Override
    public void createAdminPrincipal(String authId, String username,
                                     String password) {
        List<IdentifierValue> identifiers = List.of(
                IdentifierValue.ofUsername(username)
        );

        CreateAdminCommand command = new CreateAdminCommand(
                identifiers,
                password,
                authId,
                UserType.TEACHER
        );

        commandBus.dispatch(command);
    }

    @Override
    public void createTeacherPrincipal(String authId, List<IdentifierValue> identifiers,
                                       String password) {
        CreatePrincipalCommand command = new CreatePrincipalCommand(
                UserType.TEACHER,
                authId,
                identifiers,
                password,
                Set.of()
        );

        commandBus.dispatch(command);
    }
}
