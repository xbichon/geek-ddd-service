package vip.geekclub.manager.adapter.provided;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.command.CommandDispatcher;
import vip.geekclub.manager.application.port.SecurityServicePort;
import vip.geekclub.manager.application.port.dto.TeacherCredential;
import vip.geekclub.security.application.command.dto.CredentialDto;
import vip.geekclub.security.application.command.principal.CreatePrincipalCommand;
import vip.geekclub.security.domain.value.CredentialType;
import java.util.Set;

@Service
@AllArgsConstructor
public class SecurityServicePortImpl implements SecurityServicePort {

    @Override
    public void createCredential(TeacherCredential teacherCredential) {
        var credentialDto = new CredentialDto(
                teacherCredential.identifier(),
                teacherCredential.password(),
                teacherCredential.credentialType()
        );

        CommandDispatcher.dispatch(new CreatePrincipalCommand(
                teacherCredential.identifier(),
                teacherCredential.authId(),
                "manager",
                Set.of(credentialDto),
                teacherCredential.roleIds()
        ));
    }
}
