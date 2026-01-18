package vip.geekclub.security.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.security.application.command.dto.DeletePermissionGroupCommand;
import vip.geekclub.security.domain.PermissionGroup;
import vip.geekclub.security.domain.PermissionGroupRepository;
import vip.geekclub.security.domain.PermissionRepository;

@AllArgsConstructor
@Service
public class DeletePermissionGroupCommandHandler implements CommandHandler<DeletePermissionGroupCommand, Void> {

    private final PermissionGroupRepository permissionGroupRepository;
    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(DeletePermissionGroupCommand command) {
        // 1. 获取权限组
        PermissionGroup permissionGroup = permissionGroupRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("权限组不存在"));

        // 2. 检查是否有权限关联到该权限组
        boolean hasPermissions = permissionRepository.existsByPermissionGroupId(command.id());
        AssertUtil.isFalse(hasPermissions, () -> "该权限组下还有权限，不能删除");

        // 3. 删除权限组
        permissionGroupRepository.delete(permissionGroup);

        // 4. 返回成功结果
        return CommandResult.ok("权限组删除成功");
    }
}