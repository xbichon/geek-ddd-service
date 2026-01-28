package vip.geekclub.security.application.command.principal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.security.domain.value.Identifier;
import vip.geekclub.security.domain.model.PasswordCredential;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;
import vip.geekclub.security.domain.repository.PrincipalRepository;
import vip.geekclub.security.domain.value.IdentifierType;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

import java.util.List;

@AllArgsConstructor
@Service
public class CreateAdminCommandHandler implements CommandHandler<CreateAdminCommand, Void> {

    private final PrincipalRepository principalRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(CreateAdminCommand command) {
        // 1. 检查是否已存在超级管理员
        if (principalRepository.existsByIsSuperAdminTrue()) {
            throw new BusinessException(500, "超级管理员已存在，无需重复创建");
        }

        // 2. 认证信息查重（检查用户名是否已存在）
        if (passwordCredentialRepository.existsByIdentifierTypeAndValue(
                IdentifierType.USERNAME, command.username())) {
            throw new AuthenticationAlreadyExistsException("该用户名已被使用");
        }

        // 3. 创建超级管理员领域对象
        Principal admin = Principal.newAdmin(command.userType(), command.authId());
        principalRepository.save(admin);

        // 4. 创建用户名密码认证信息
        PasswordCredential credential = PasswordCredential.create(
                admin.getId(),
                List.of(new Identifier(command.username(), IdentifierType.USERNAME)), command.password()
        );
        passwordCredentialRepository.save(credential);

        return CommandResult.ok();
    }
}