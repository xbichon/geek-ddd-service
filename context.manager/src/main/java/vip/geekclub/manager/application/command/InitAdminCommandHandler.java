package vip.geekclub.manager.application.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.manager.application.command.dto.InitAdminCommand;

@Service
@RequiredArgsConstructor
public class InitAdminCommandHandler implements CommandHandler<InitAdminCommand, Void> {
    @Override
    public CommandResult<Void> execute(InitAdminCommand command) {
        return null;
    }
}
