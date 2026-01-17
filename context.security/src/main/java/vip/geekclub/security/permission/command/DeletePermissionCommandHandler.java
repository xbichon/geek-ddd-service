package vip.geekclub.security.permission.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.security.permission.command.dto.DeletePermissionCommand;
import vip.geekclub.security.permission.domain.PermissionRepository;

@AllArgsConstructor
@Service
public class DeletePermissionCommandHandler implements CommandHandler<DeletePermissionCommand, Void> {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(DeletePermissionCommand command) {
        // 删除权限
        permissionRepository.deleteById(command.id());
        
        return CommandResult.ok();
    }
}