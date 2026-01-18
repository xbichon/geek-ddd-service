package vip.geekclub.initialize;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.security.application.command.dto.InitAdminCommand;

@Component
@AllArgsConstructor
public class InitAdminUserRunner implements CommandLineRunner {

    private CommandBus commandBus;

    @Override
    @Async
    public void run(@Nullable String... args) {
        commandBus.dispatch(new InitAdminCommand());
    }
}