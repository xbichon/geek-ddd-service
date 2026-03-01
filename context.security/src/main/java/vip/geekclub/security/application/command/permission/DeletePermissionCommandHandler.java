package vip.geekclub.security.application.command.permission;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.security.domain.authorization.repository.PermissionRepository;

@AllArgsConstructor
@Service
public class DeletePermissionCommandHandler implements VoidCommandHandler<DeletePermissionCommand> {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public void executeVoid(DeletePermissionCommand command) {
        // 删除权限
        permissionRepository.deleteById(command.id());
    }
}