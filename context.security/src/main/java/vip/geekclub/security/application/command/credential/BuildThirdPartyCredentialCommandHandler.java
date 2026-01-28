package vip.geekclub.security.application.command.credential;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.model.ThirdPartyCredential;
import vip.geekclub.security.domain.repository.PrincipalRepository;
import vip.geekclub.security.domain.repository.ThirdPartyCredentialRepository;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

@AllArgsConstructor
@Service
public class BuildThirdPartyCredentialCommandHandler implements CommandHandler<BuildThirdPartyCredentialCommand, Void> {

    private final ThirdPartyCredentialRepository thirdPartyCredentialRepository;
    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(BuildThirdPartyCredentialCommand command) {
        // 1. 查询用户
        Principal principal = principalRepository.findByAuthId(command.authId())
                .orElseThrow(() -> new NotFoundException("用户不存在"));

        // 2. 认证信息查重
        if (thirdPartyCredentialRepository.existsByTypeAndPrincipalId(command.type(), principal.getId())) {
            throw new AuthenticationAlreadyExistsException("该第三方认证已存在");
        }

        // 3. 认证标识查重
        if (thirdPartyCredentialRepository.existsByTypeAndCode(command.type(), command.identifier())) {
            throw new AuthenticationAlreadyExistsException("该第三方标识符已被使用");
        }

        // 4. 创建第三方凭证
        ThirdPartyCredential credential = new ThirdPartyCredential(principal.getId(), command.type(), command.identifier());
        thirdPartyCredentialRepository.save(credential);

        return CommandResult.ok();
    }
}