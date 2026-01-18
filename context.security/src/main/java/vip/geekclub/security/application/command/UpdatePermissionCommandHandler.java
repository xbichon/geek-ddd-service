package vip.geekclub.security.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.security.application.command.dto.UpdatePermissionCommand;
import vip.geekclub.security.domain.Permission;
import vip.geekclub.security.domain.PermissionRepository;
import vip.geekclub.security.domain.PermissionCode;

@AllArgsConstructor
@Service
public class UpdatePermissionCommandHandler implements CommandHandler<UpdatePermissionCommand, Void> {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(UpdatePermissionCommand command) {
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
        
        return CommandResult.ok("权限更新成功");
    }
}