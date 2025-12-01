package vip.geekclub.config.command;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.common.command.Command;
import vip.geekclub.common.command.CommandInterceptor;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CommandValidatorInterceptor implements CommandInterceptor {

    private final Validator validator;

    @Override
    public void beforeHandle(Command command) {
        Set<ConstraintViolation<Command>> violations = validator.validate(command);
        if (violations.isEmpty()) return;

        throw new ConstraintViolationException(violations);
    }
}
