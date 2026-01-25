package vip.geekclub.framework.command;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;


/**
 * 命令分发器
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CommandDispatcher implements ApplicationContextAware {

    private static CommandBus commandBus;

    public static <C extends Command, R> CommandResult<R> dispatch(C command) {
        return commandBus.dispatch(command);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("初始化命令分发器...");
        commandBus = applicationContext.getBean(CommandBus.class);
    }
}