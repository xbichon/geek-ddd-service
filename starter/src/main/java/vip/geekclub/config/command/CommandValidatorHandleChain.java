package vip.geekclub.config.command;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandHandlerChain;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandValidatorHandleChain extends CommandHandlerChain {

    private final Validator validator;

    @Override
    @SuppressWarnings("unchecked")
    public <R> R handle(Command<R> command) {
        Set<ConstraintViolation<Command<R>>> violations = validator.validate(command);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return next(command);
    }
}
