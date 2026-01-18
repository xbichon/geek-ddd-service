package vip.geekclub.security.auth.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.auth.application.command.dto.DeleteUserCommand;
import vip.geekclub.security.auth.domain.Principal;
import vip.geekclub.security.auth.domain.PrincipalRepository;

@AllArgsConstructor
@Service
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommand, Void> {

    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(DeleteUserCommand command) {
        // 1. 获取用户
        Principal principal = principalRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("用户不存在"));

        // 2. 清除角色并验证是否可删除
        principal.clearRole();
        principal.validateDeletable();

        // 3. 删除用户
        principalRepository.delete(principal);
        return CommandResult.ok();
    }
}