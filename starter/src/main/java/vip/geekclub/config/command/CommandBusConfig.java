package vip.geekclub.config.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import vip.geekclub.framework.command.*;

import java.util.List;

@Slf4j
@Configuration
public class  CommandBusConfig {

    @Lazy
    @Bean
    public CommandBus commandBus(List<CommandHandler<?,?>> commandHandlers,CommandValidatorHandleChain commandValidatorHandleChain) {
        log.info("初始化命令总线...");
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.addHandlers(commandHandlers);
        commandBus.addChain(commandValidatorHandleChain);
        return commandBus;
    }
}
