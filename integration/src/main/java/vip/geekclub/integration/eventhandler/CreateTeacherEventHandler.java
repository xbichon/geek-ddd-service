package vip.geekclub.integration.eventhandler;

import lombok.AllArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.command.CommandBus;
import vip.geekclub.manager.application.command.dto.CreateTeacherCommand;
import vip.geekclub.manager.domain.event.UserCreatedEvent;
import vip.geekclub.security.application.command.dto.CreatePrincipalCommand;

@Service
@AllArgsConstructor
public class CreateTeacherEventHandler {

    private final CommandBus commandBus;

    @EventListener
    public void handle(UserCreatedEvent event) {

        CreatePrincipalCommand command = new CreatePrincipalCommand(

        );

    }
}
