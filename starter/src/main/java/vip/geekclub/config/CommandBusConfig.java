package vip.geekclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vip.geekclub.common.command.*;

import java.util.List;

@Configuration
public class CommandBusConfig {

    @Bean
    public CommandBus commandBus(List<CommandHandler<?,?>> commandHandlers) {
        SimpleCommandBus commandBus = new SimpleCommandBus();
        commandBus.addHandlers(commandHandlers);

        return commandBus;
    }
}
