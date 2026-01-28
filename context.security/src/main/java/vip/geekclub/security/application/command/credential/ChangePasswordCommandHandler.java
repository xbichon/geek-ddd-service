package vip.geekclub.security.application.command.credential;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.InvalidCredentialsException;
import vip.geekclub.security.domain.model.PasswordCredential;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;

@AllArgsConstructor
@Service
public class ChangePasswordCommandHandler implements CommandHandler<ChangePasswordCommand, Void> {

    private final PasswordCredentialRepository passwordCredentialRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(ChangePasswordCommand command) {
        PasswordCredential credential = passwordCredentialRepository.findByIdentifiersValue(command.authId())
                .orElseThrow(() -> new InvalidCredentialsException("账户未找到"));

        credential.changePassword(command.oldPassword(), command.newPassword());

        return CommandResult.ok();
    }
}