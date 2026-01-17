package vip.geekclub.security.auth.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.security.auth.application.command.dto.CreateWechatCredentialCommand;
import vip.geekclub.security.auth.domain.AuthenticationType;
import vip.geekclub.security.auth.domain.Credential;
import vip.geekclub.security.auth.domain.CredentialRepository;
import vip.geekclub.security.permission.exception.AuthenticationAlreadyExistsException;

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
