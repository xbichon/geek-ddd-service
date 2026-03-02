package vip.geekclub.security.application.command.permission;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.security.domain.authorization.model.Permission;
import vip.geekclub.security.domain.authorization.repository.PermissionRepository;
import vip.geekclub.security.domain.authorization.value.PermissionCode;

@AllArgsConstructor
@Service
public class CreatePermissionCommandHandler implements CommandHandler<CreatePermissionCommand, Long> {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public Long execute(CreatePermissionCommand command) {
        // 1. 验证权限编码不存在
        if (permissionRepository.existsByCode(command.code())) {
            throw new BusinessException("权限编码已存在");
        }

        // 2. 创建权限领域对象
        Permission permission = new Permission(
                command.userType(),
                command.name(),
                PermissionCode.of(command.code()),
                command.description(),
                command.permissionGroupId()
        );
        permissionRepository.save(permission);

        // 3. 返回权限ID
        return permission.getId();
    }
}