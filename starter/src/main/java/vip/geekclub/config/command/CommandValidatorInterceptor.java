package vip.geekclub.config.command;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.common.command.Command;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.command.CommandHandlerChain;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandValidatorInterceptor extends CommandHandlerChain {

    private final Validator validator;

    @Override
    protected CommandResult handle(Command command, CommandHandlerChain chain) {
        Set<ConstraintViolation<Command>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return chain.handle(command);
    }
}
