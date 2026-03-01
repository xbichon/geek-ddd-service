package vip.geekclub.security.application.command.principal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.security.domain.service.IdentifierValidate;
import vip.geekclub.security.domain.value.IdentifierValue;
import vip.geekclub.security.domain.model.PasswordCredential;
import vip.geekclub.security.domain.model.Principal;
import vip.geekclub.security.domain.model.Role;
import vip.geekclub.security.domain.repository.PasswordCredentialRepository;
import vip.geekclub.security.domain.repository.PrincipalRepository;
import vip.geekclub.security.domain.repository.RoleRepository;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

import java.util.Set;

@AllArgsConstructor
@Service
public class CreateAdminCommandHandler implements VoidCommandHandler<CreateAdminCommand> {

    private final PrincipalRepository principalRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;
    private final RoleRepository roleRepository;
    private final IdentifierValidate identifierValidate;

    @Override
    @Transactional
    public void executeVoid(CreateAdminCommand command) {
        // 验证标识符
        identifierValidate.validate(command.identifierValues());

        // 认证信息查重（检查用户名是否已存在）
        for (IdentifierValue identifierValue : command.identifierValues()) {
            if (passwordCredentialRepository.existsByIdentifier(
                    identifierValue.value(), identifierValue.type())) {
                throw new AuthenticationAlreadyExistsException("该用户名已被使用");
            }
        }

        // 获取或创建系统管理员角色
        Role systemAdminRole = roleRepository.findByUserTypeAndSystemAdmin(command.userType(), true)
                .orElseGet(() -> {
                    Role newRole = Role.createSystemAdminRole(command.userType());
                    return roleRepository.save(newRole);
                });

        // 创建管理员领域对象，关联系统管理员角色
        Principal admin = new Principal(
                command.userType(),
                command.authId(),
                Set.of(systemAdminRole.getId())
        );
        principalRepository.save(admin);

        // 创建用户名密码认证信息
        PasswordCredential credential = PasswordCredential.create(
                admin.getId(),
                admin.getAuthId(),
                command.identifierValues(),
                command.password(),
                command.userType()
        );
        passwordCredentialRepository.save(credential);
    }
}