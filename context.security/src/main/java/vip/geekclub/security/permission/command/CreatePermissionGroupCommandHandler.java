package vip.geekclub.security.permission.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.command.IdResult;
import vip.geekclub.common.exception.ValidationException;
import vip.geekclub.security.permission.command.dto.CreatePermissionGroupCommand;
import vip.geekclub.security.permission.domain.PermissionGroup;
import vip.geekclub.security.permission.domain.PermissionGroupRepository;
import vip.geekclub.security.permission.domain.SortOrder;

@AllArgsConstructor
@Service
public class CreatePermissionGroupCommandHandler implements CommandHandler<CreatePermissionGroupCommand, IdResult> {

    private final PermissionGroupRepository permissionGroupRepository;

    @Override
    @Transactional
    public CommandResult<IdResult> execute(CreatePermissionGroupCommand command) {
        // 1. 验证权限组名称不存在
        if (permissionGroupRepository.existsByName(command.name())) {
            throw new ValidationException("权限组名称已存在");
        }
        
        // 2. 创建权限组领域对象
        PermissionGroup permissionGroup = PermissionGroup.createPermissionGroup(
            command.name(),
            command.description(),
            SortOrder.of(command.sortOrder())
        );
        permissionGroupRepository.save(permissionGroup);
        
        return CommandResult.ok(permissionGroup.getId());
    }
}