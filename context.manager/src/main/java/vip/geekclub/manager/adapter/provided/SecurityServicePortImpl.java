package vip.geekclub.manager.adapter.provided;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.command.CommandDispatcher;
import vip.geekclub.manager.application.port.SecurityServicePort;
import vip.geekclub.manager.application.port.dto.TeacherCredential;
import vip.geekclub.security.application.command.dto.CreatePrincipalCommand;
import vip.geekclub.security.domain.value.CredentialType;

@Service
@AllArgsConstructor
public class SecurityServicePortImpl implements SecurityServicePort {

    @Override
    public void createCredential(TeacherCredential teacherCredential) {
        CommandDispatcher.dispatch(new CreatePrincipalCommand(
                teacherCredential.username(),
                teacherCredential.password(),
                teacherCredential.authId(),
                "manager",
                CredentialType.USERNAME
        ));
    }
}
