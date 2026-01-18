package vip.geekclub.security.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.security.application.command.dto.CreateWechatCredentialCommand;
import vip.geekclub.security.domain.AuthenticationType;
import vip.geekclub.security.domain.Credential;
import vip.geekclub.security.domain.CredentialRepository;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

@Service
@AllArgsConstructor
public class CreateWechatCredentialCommandHandler implements CommandHandler<CreateWechatCredentialCommand, Void> {

    private final CredentialRepository credentialRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(CreateWechatCredentialCommand command) {
        // 幂等校验：同一 unionId 是否已被绑定
        if (credentialRepository.existsByTypeAndIdentifier(AuthenticationType.WECHAT, command.unionId())) {
            throw new AuthenticationAlreadyExistsException("该微信UnionID已绑定，无需重复绑定");
        }

        // 创建并保存微信凭证
        Credential wechatCredential = Credential.newWechatAuth(command.userId(), command.unionId());
        credentialRepository.save(wechatCredential);

        // 返回成功结果
        return CommandResult.ok();
    }
}
