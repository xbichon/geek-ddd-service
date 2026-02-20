package vip.geekclub.config.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import vip.geekclub.framework.command.*;

@Slf4j
@Configuration
public class  CommandBusConfig {

    @Lazy
    @Bean
    public CommandBus commandBus(CommandValidatorHandleChain commandValidatorHandleChain) {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.addChain(commandValidatorHandleChain);
        return commandBus;
    }
}
