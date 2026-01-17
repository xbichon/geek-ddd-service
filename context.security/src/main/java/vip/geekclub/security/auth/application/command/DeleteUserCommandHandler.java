package vip.geekclub.security.auth.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.exception.NotFoundException;
import vip.geekclub.security.auth.application.command.dto.DeleteUserCommand;
import vip.geekclub.security.auth.domain.UserPrincipal;
import vip.geekclub.security.auth.domain.UserPrincipalRepository;

@AllArgsConstructor
@Service
public class DeleteUserCommandHandler implements CommandHandler<DeleteUserCommand, Void> {

    private final UserPrincipalRepository userPrincipalRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(DeleteUserCommand command) {
        // 1. 获取用户
        UserPrincipal userPrincipal = userPrincipalRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("用户不存在"));

        // 2. 清除角色并验证是否可删除
        userPrincipal.clearRole();
        userPrincipal.validateDeletable();

        // 3. 删除用户
        userPrincipalRepository.delete(userPrincipal);
        return CommandResult.ok();
    }
}