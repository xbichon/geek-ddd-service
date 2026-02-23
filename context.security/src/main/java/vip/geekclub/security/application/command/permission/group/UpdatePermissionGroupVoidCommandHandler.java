package vip.geekclub.security.application.command.permission.group;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.security.domain.model.PermissionGroup;
import vip.geekclub.security.domain.repository.PermissionGroupRepository;
import vip.geekclub.security.domain.value.SortOrder;

@AllArgsConstructor
@Service
public class UpdatePermissionGroupVoidCommandHandler implements VoidCommandHandler<UpdatePermissionGroupCommand> {

    private final PermissionGroupRepository permissionGroupRepository;

    @Override
    @Transactional
    public void executeVoid(UpdatePermissionGroupCommand command) {
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
    }
}