package vip.geekclub.config.commandBus;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.geekclub.common.command.*;

import java.util.List;

@Configuration
public class CommandBusConfig {

    @Bean
    public CommandBus commandBus(List<CommandHandler<?,?>> commandHandlers,CommandValidatorHandleChain commandValidatorHandleChain) {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.addHandlers(commandHandlers);
        commandBus.addChain(commandValidatorHandleChain);

        return commandBus;
    }
}
