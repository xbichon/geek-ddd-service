package vip.geekclub.education.students.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vip.geekclub.common.command.VoidCommandHandler;
import vip.geekclub.education.students.command.dto.RemoveStudntCommand;
import vip.geekclub.education.students.domain.StudentRepository;

@Service
@AllArgsConstructor
public class RemoveStudentCommandHandler implements VoidCommandHandler<RemoveStudntCommand> {
    private StudentRepository studentRepository;

    @Override
    public void process(RemoveStudntCommand command) {
        studentRepository.deleteById(command.id());
    }
}
