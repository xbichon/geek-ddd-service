package vip.geekclub.security.application.command.permission;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.security.domain.model.Permission;
import vip.geekclub.security.domain.repository.PermissionRepository;
import vip.geekclub.security.domain.value.PermissionCode;

@AllArgsConstructor
@Service
public class UpdatePermissionVoidCommandHandler implements VoidCommandHandler<UpdatePermissionCommand> {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void executeVoid(UpdatePermissionCommand command) {
        // 1. 查询权限是否存在
        Permission permission = permissionRepository.findById(command.id())
            .orElseThrow(() -> new NotFoundException("权限不存在"));

        // 2. 验证权限码不重复
        if (!permission.getPermissionCode().code().equals(command.code()) &&
            permissionRepository.existsByCode(command.code())) {
            throw new ValidationException("权限码已经存在");
        }

        // 3. 更新权限信息
        permission.updateInfo(
            command.name(),
            PermissionCode.of(command.code()),
            command.description()
        );
    }
}