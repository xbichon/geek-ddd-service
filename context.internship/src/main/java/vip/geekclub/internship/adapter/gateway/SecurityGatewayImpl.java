package vip.geekclub.internship.adapter.gateway;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.contract.UserType;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.internship.application.gateway.SecurityGateway;
import vip.geekclub.security.application.command.principal.CreatePrincipalCommand;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.List;

/**
 * 安全模块防腐层实现
 */
@Component
@RequiredArgsConstructor
public class SecurityGatewayImpl implements SecurityGateway {

    private final CommandBus commandBus;

    @Override
    public void createStudentPrincipal(String authId, String studentNo,
                                       String password) {
        List<IdentifierValue> identifiers = List.of(
                new IdentifierValue("STUDENT_NO", studentNo)
        );

        CreatePrincipalCommand command = new CreatePrincipalCommand(
                UserType.STUDENT,
                authId,
                identifiers,
                password
        );

        commandBus.dispatch(command);
    }
}
