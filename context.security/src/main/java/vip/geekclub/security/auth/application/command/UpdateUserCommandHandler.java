package vip.geekclub.security.auth.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.auth.application.command.dto.UpdateUserCommand;
import vip.geekclub.security.auth.domain.Principal;
import vip.geekclub.security.auth.domain.PrincipalRepository;

@AllArgsConstructor
@Service
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, Void> {

    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(UpdateUserCommand command) {
        // 1. 获取用户
        Principal principal = principalRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("用户不存在"));

        // 2. 更新用户信息
        principal.updateRole(command.roles());

        // 3. 保存用户
        principalRepository.save(principal);
        return CommandResult.ok();
    }
}