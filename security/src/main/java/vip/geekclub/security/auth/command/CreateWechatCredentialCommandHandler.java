package vip.geekclub.security.auth.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.VoidCommandHandler;
import vip.geekclub.security.auth.command.dto.CreateWechatCredentialCommand;
import vip.geekclub.security.auth.common.AuthenticationType;
import vip.geekclub.security.auth.domain.Credential;
import vip.geekclub.security.auth.domain.CredentialRepository;
import vip.geekclub.security.permission.exception.AuthenticationAlreadyExistsException;

@Service
@AllArgsConstructor
public class CreateWechatCredentialCommandHandler implements VoidCommandHandler<CreateWechatCredentialCommand> {

    private final CredentialRepository credentialRepository;

    @Override
    @Transactional
    public void process(CreateWechatCredentialCommand command) {
        // 幂等校验：同一 unionId 是否已被绑定
        if (credentialRepository.existsByTypeAndIdentifier(AuthenticationType.WECHAT, command.unionId())) {
            throw new AuthenticationAlreadyExistsException("该微信UnionID已绑定，无需重复绑定");
        }

        // 创建并保存微信凭证
        Credential wechatCredential = Credential.newWechatAuth(command.userId(), command.unionId());
        credentialRepository.save(wechatCredential);
    }
}
