package vip.geekclub.security.application.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.application.command.dto.UpdatePrincipalRoleCommand;
import vip.geekclub.security.domain.Principal;
import vip.geekclub.security.domain.PrincipalRepository;

@AllArgsConstructor
@Service
public class UpdatePrincipalRoleCommandHandler implements CommandHandler<UpdatePrincipalRoleCommand, Void> {

    private final PrincipalRepository principalRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(UpdatePrincipalRoleCommand command) {
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