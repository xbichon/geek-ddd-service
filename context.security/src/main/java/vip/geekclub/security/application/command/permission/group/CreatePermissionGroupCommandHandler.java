package vip.geekclub.security.application.command.permission.group;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.security.domain.authorization.model.PermissionGroup;
import vip.geekclub.security.domain.authorization.repository.PermissionGroupRepository;
import vip.geekclub.contract.value.SortOrder;

@AllArgsConstructor
@Service
public class CreatePermissionGroupCommandHandler implements CommandHandler<CreatePermissionGroupCommand, Long> {

    private final PermissionGroupRepository permissionGroupRepository;

    @Override
    @Transactional
    public Long execute(CreatePermissionGroupCommand command) {
        // 1. 验证权限组名称不存在
        if (permissionGroupRepository.existsByName(command.name())) {
            throw new ValidationException("权限组名称已存在");
        }

        // 2. 创建权限组领域对象
        PermissionGroup permissionGroup = PermissionGroup.createPermissionGroup(
                command.userType(),
                command.name(),
                command.description(),
                SortOrder.of(command.sortOrder())
        );
        permissionGroupRepository.save(permissionGroup);

        return permissionGroup.getId();
    }
}