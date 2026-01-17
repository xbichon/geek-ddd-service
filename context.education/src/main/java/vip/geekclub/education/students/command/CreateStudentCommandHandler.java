package vip.geekclub.education.students.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.education.students.command.dto.CreateStudentCommand;
import vip.geekclub.education.students.domain.Student;
import vip.geekclub.education.students.domain.StudentRepository;

@Service // 对应创建学生的处理器
@AllArgsConstructor  // 构造函数
public class CreateStudentCommandHandler implements CommandHandler<CreateStudentCommand, Void> {

    private final StudentRepository studentRepository;

    @Override
    public CommandResult<Void> execute(CreateStudentCommand command) {

        // 根据命令 创建一个 Student
        Student student = new Student(command.name(), command.sex());

        // 保存到数据库
        studentRepository.save(student);

        // 创建成功
        return CommandResult.ok();
    }
}
