package vip.geekclub.manager.command;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.common.command.CommandHandler;
import vip.geekclub.common.command.CommandResult;
import vip.geekclub.common.exception.ValidationException;
import vip.geekclub.manager.command.dto.DeleteTeacherCommand;
import vip.geekclub.manager.domain.Teacher;
import vip.geekclub.manager.domain.TeacherRepository;

/**
 * 删除教师命令处理器
 */
@Transactional
@RequiredArgsConstructor
@Service
public class DeleteTeacherCommandHandler implements CommandHandler<DeleteTeacherCommand, Void> {

    private final TeacherRepository teacherRepository;

    @Override
    public CommandResult<Void> execute(DeleteTeacherCommand command) {
        // 1. 获取教师
        Teacher teacher = teacherRepository.findById(command.id())
                .orElseThrow(() -> new ValidationException("指定的教师不存在"));

        // 2. 验证是否可以删除
        teacher.validateDeletable();

        // 3. 删除教师
        teacherRepository.delete(teacher);

        // 4. 返回成功结果
        return CommandResult.ok();
    }
}