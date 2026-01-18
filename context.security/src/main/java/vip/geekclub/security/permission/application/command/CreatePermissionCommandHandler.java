package vip.geekclub.security.permission.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.command.IdResult;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.security.permission.domain.Permission;
import vip.geekclub.security.permission.domain.PermissionRepository;
import vip.geekclub.security.permission.domain.PermissionCode;
import vip.geekclub.security.permission.application.command.dto.CreatePermissionCommand;

@AllArgsConstructor
@Service
public class CreatePermissionCommandHandler implements CommandHandler<CreatePermissionCommand, IdResult> {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public CommandResult<IdResult> execute(CreatePermissionCommand command) {
        // 1. 验证权限编码不存在
        if (permissionRepository.existsByCode(command.code())) {
            throw new ValidationException("权限编码已存在");
        }
        
        // 2. 创建权限领域对象
        Permission permission = new Permission(
            command.name(),
            PermissionCode.of(command.code()),
            command.description(),
            command.permissionGroupId()
        );
        permissionRepository.save(permission);

        // 3. 返回权限ID
        return CommandResult.ok(permission.getId());
    }
}