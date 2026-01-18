package vip.geekclub.education.students.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.education.students.command.dto.RemoveStudntCommand;
import vip.geekclub.education.students.domain.StudentRepository;

@Service
@AllArgsConstructor
public class RemoveStudentCommandHandler implements CommandHandler<RemoveStudntCommand, Void> {
    private StudentRepository studentRepository;

    @Override
    public CommandResult<Void> execute(RemoveStudntCommand command) {
        studentRepository.deleteById(command.id());
        return CommandResult.ok();
    }
}
