package vip.geekclub.security.auth.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.exception.NotFoundException;
import vip.geekclub.security.auth.command.dto.UpdateUserCommand;
import vip.geekclub.security.auth.domain.UserPrincipal;
import vip.geekclub.security.auth.domain.UserPrincipalRepository;

@AllArgsConstructor
@Service
public class UpdateUserCommandHandler implements CommandHandler<UpdateUserCommand, Void> {

    private final UserPrincipalRepository userPrincipalRepository;

    @Override
    @Transactional
    public CommandResult<Void> execute(UpdateUserCommand command) {
        // 1. 获取用户
        UserPrincipal userPrincipal = userPrincipalRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("用户不存在"));

        // 2. 更新用户信息
        userPrincipal.updateRole(command.roles());

        // 3. 保存用户
        userPrincipalRepository.save(userPrincipal);
        return CommandResult.ok();
    }
}