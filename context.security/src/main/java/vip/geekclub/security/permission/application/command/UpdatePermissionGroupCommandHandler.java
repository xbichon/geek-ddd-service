package vip.geekclub.security.permission.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.exception.NotFoundException;
import vip.geekclub.common.exception.ValidationException;
import vip.geekclub.security.permission.application.command.dto.UpdatePermissionGroupCommand;
import vip.geekclub.security.permission.domain.PermissionGroup;
import vip.geekclub.security.permission.domain.PermissionGroupRepository;
import vip.geekclub.security.permission.domain.SortOrder;

@AllArgsConstructor
@Service
public class UpdatePermissionGroupCommandHandler implements CommandHandler<UpdatePermissionGroupCommand, Void> {

    private final PermissionGroupRepository permissionGroupRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(UpdatePermissionGroupCommand command) {
        // 1. 查询权限组是否存在
        PermissionGroup permissionGroup = permissionGroupRepository.findById(command.id())
            .orElseThrow(() -> new NotFoundException("权限组不存在"));
        
        // 2. 验证权限组名称不重复
        if (!permissionGroup.getName().equals(command.name()) && 
            permissionGroupRepository.existsByName(command.name())) {
            throw new ValidationException("权限组名称已经存在");
        }
        
        // 3. 更新权限组信息
        permissionGroup.updatePermissionGroup(
            command.name(),
            command.description(),
            SortOrder.of(command.sortOrder())
        );
        
        return CommandResult.ok("权限组更新成功");
    }
}