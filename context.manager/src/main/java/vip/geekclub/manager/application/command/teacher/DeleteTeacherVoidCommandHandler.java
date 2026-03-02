package vip.geekclub.manager.application.command.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.manager.domain.model.Teacher;
import vip.geekclub.manager.domain.repository.TeacherRepository;

/**
 * 删除教师命令处理器
 */
@Transactional
@RequiredArgsConstructor
@Service
public class DeleteTeacherVoidCommandHandler implements VoidCommandHandler<DeleteTeacherCommand> {

    private final TeacherRepository teacherRepository;

    @Override
    public void executeVoid(DeleteTeacherCommand command) {
        // 1. 获取教师
        Teacher teacher = teacherRepository.findById(command.id())
                .orElseThrow(() -> new NotFoundException("指定的教师不存在"));

        // 2. 验证是否可以删除
        teacher.validateDeletable();

        // 3. 删除教师
        teacherRepository.delete(teacher);
    }
}