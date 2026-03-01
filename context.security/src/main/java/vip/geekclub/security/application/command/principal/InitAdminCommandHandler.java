package vip.geekclub.security.application.command.principal;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.security.domain.authentication.model.Identifier;
import vip.geekclub.security.domain.authentication.model.Password;
import vip.geekclub.security.domain.authentication.service.IdentifierValidate;
import vip.geekclub.security.domain.authentication.value.IdentifierValue;
import vip.geekclub.security.domain.authentication.model.Principal;
import vip.geekclub.security.domain.authorization.model.Role;
import vip.geekclub.security.domain.authentication.repository.IdentifierRepository;
import vip.geekclub.security.domain.authentication.repository.PasswordCredentialRepository;
import vip.geekclub.security.domain.authentication.repository.PrincipalRepository;
import vip.geekclub.security.domain.authorization.repository.RoleRepository;
import vip.geekclub.security.exception.AuthenticationAlreadyExistsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class InitAdminCommandHandler implements VoidCommandHandler<InitAdminCommand> {

    private final PrincipalRepository principalRepository;
    private final PasswordCredentialRepository passwordCredentialRepository;
    private final RoleRepository roleRepository;
    private final IdentifierRepository identifierRepository;
    private final IdentifierValidate identifierValidate;

    @Override
    @Transactional
    public void executeVoid(InitAdminCommand command) {
        // 验证标识符
        identifierValidate.validate(command.identifierValues());

        Integer systemAdminCount = roleRepository.countByUserTypeAndSystemAdminIsTrue(command.userType());
        if (systemAdminCount > 0) {
            throw new BusinessException(500, "系统已存在系统管理员");
        }

        // 认证信息查重（检查用户名是否已存在）
        for (IdentifierValue identifierValue : command.identifierValues()) {
            if (identifierRepository.existsByValueAndUserType(identifierValue.value(), command.userType())) {
                throw new AuthenticationAlreadyExistsException("该用户名已被使用");
            }
        }

        // 获取或创建系统管理员角色
        Role systemAdminRole = Role.createSystemAdminRole(command.userType());
        roleRepository.save(systemAdminRole);

        // 创建管理员领域对象，关联系统管理员角色
        Principal admin = new Principal(
                command.userType(),
                command.authId(),
                Set.of(systemAdminRole.getId())
        );
        principalRepository.save(admin);

        // 创建标识符
        List<Identifier> identifiers = new ArrayList<>();
        for (IdentifierValue identifierValue : command.identifierValues()) {
            Identifier identifier = new Identifier(
                    identifierValue.value(),
                    identifierValue.type(),
                    command.userType(),
                    admin.getId()
            );
            identifiers.add(identifier);
        }
        identifierRepository.saveAll(identifiers);

        // 创建用户名密码认证信息
        Password credential = Password.create(
                admin.getId(),
                admin.getAuthId(),
                command.password()
        );
        passwordCredentialRepository.save(credential);
    }
}