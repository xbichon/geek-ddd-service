package vip.geekclub.education.students.command;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vip.geekclub.common.command.VoidCommandHandler;
import vip.geekclub.education.students.command.dto.CreateStudentCommand;
import vip.geekclub.education.students.domain.Student;
import vip.geekclub.education.students.domain.StudentRepository;

@Service // 对应创建学生的处理器
@AllArgsConstructor  // 构造函数
public class CreateStudentCommandHandler implements VoidCommandHandler<CreateStudentCommand> {

    private final StudentRepository studentRepository;

    @Override
    public void process(CreateStudentCommand command) {
        // 根据命令 创建一个 Student
        Student student = new Student(command.name(), command.sex());

        // 保存到数据库
        studentRepository.save(student);
    }
}
