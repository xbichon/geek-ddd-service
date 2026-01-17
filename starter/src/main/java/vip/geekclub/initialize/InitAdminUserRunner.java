package vip.geekclub.initialize;

import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import vip.geekclub.common.command.CommandBus;
import vip.geekclub.security.auth.command.dto.InitAdminCommand;

@Component
@AllArgsConstructor
public class InitAdminUserRunner implements CommandLineRunner {

    private CommandBus commandBus;

    @Override
    @Async
    public void run(String... args) {
        commandBus.dispatch(new InitAdminCommand());
    }
}