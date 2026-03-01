package vip.geekclub.security.application.command.principal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.security.domain.authentication.model.Identifier;
import vip.geekclub.security.domain.authentication.model.Password;
import vip.geekclub.security.domain.authentication.model.Principal;
import vip.geekclub.security.domain.authentication.repository.IdentifierRepository;
import vip.geekclub.security.domain.authentication.repository.PasswordCredentialRepository;
import vip.geekclub.security.domain.authentication.repository.PrincipalRepository;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

@AllArgsConstructor
@Service
public class CreatePrincipalCommandHandler implements VoidCommandHandler<CreatePrincipalCommand> {

    private final PrincipalRepository principalRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;
    private final IdentifierRepository identifierRepository;

    @Override
    @Transactional
    public void executeVoid(CreatePrincipalCommand command) {

        // 1. 检查所有标识符是否已存在
        for (var identifier : command.identifierValues()) {
            if (identifierRepository.existsByValueAndUserType(identifier.value(), command.userType())) {
                throw new AuthenticationAlreadyExistsException("该标识符已被使用: " + identifier.value());
            }
        }

        // 2. 创建用户领域对象
        Principal principal = new Principal(command.userType(), command.authId(), command.roleIds());
        principalRepository.save(principal);

        // 4. 创建密码凭证
        Password passwordCredential = Password.create(
                principal.getId(),
                principal.getAuthId(),
                command.password()
        );
        passwordCredentialRepository.save(passwordCredential);

        // 5. 创建标识符
        for (var identifierValue : command.identifierValues()) {
            Identifier identifier = new Identifier(
                    identifierValue.value(),
                    identifierValue.type(),
                    command.userType(),
                    principal.getId()
            );
            identifierRepository.save(identifier);
        }
    }
}