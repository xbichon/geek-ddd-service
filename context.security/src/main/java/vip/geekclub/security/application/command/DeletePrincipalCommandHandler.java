package vip.geekclub.security.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.application.command.dto.DeletePrincipalCommand;
import vip.geekclub.security.domain.Principal;
import vip.geekclub.security.domain.PrincipalRepository;

@AllArgsConstructor
@Service
public class DeletePrincipalCommandHandler implements CommandHandler<DeletePrincipalCommand, Void> {

    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(DeletePrincipalCommand command) {
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