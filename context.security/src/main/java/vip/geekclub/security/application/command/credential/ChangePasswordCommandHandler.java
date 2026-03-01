package vip.geekclub.security.application.command.credential;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.security.domain.authentication.repository.PasswordCredentialRepository;

@AllArgsConstructor
@Service
public class ChangePasswordCommandHandler implements VoidCommandHandler<ChangePasswordCommand> {

    private final PasswordCredentialRepository passwordCredentialRepository;

    @Override
    @Transactional
    public void executeVoid(ChangePasswordCommand command) {
//        UserPrincipal currentPrincipal = CommandContext.getCurrentPrincipal();
//
//        PasswordCredential credential = passwordCredentialRepository.(currentPrincipal.authId())
//                .orElseThrow(() -> new InvalidCredentialsException("账户未找到"));
//
//        credential.changePassword(command.oldPassword(), command.newPassword());
    }
}