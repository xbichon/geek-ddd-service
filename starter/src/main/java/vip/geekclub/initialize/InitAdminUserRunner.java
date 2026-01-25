package vip.geekclub.initialize;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
//import vip.geekclub.framework.command.CommandBus;

@Slf4j
@Component
@AllArgsConstructor
public class InitAdminUserRunner implements CommandLineRunner {

//    private CommandBus commandBus;

    @Override
    @Async
    public void run(@Nullable String... args) {
        log.info("开始初始管理员用户...");
//        var initAdminCommand = new InitAdminCommand("admin", "123456", "TEACHER");
//        commandBus.dispatch(initAdminCommand);
        log.info("结束初始管理员用户...");
    }
}