package vip.geekclub.internship.application.command.thesisselection;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.internship.domain.model.Advisor;
import vip.geekclub.internship.domain.repository.AdvisorRepository;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class BatchInitAdvisorCommandHandler implements CommandHandler<BatchInitAdvisorCommand, List<Long>> {

    private final AdvisorRepository advisorRepository;

    @Override
    @Transactional
    public CommandResult<List<Long>> execute(BatchInitAdvisorCommand command) {
        // 1. 查询指导教师表是否为空
        boolean isEmpty = advisorRepository.count() == 0;

        if (!isEmpty) {
            return CommandResult.ok(List.of());
        }

        // 2. 初始化指导教师
        List<Long> advisorIds = new ArrayList<>();
        int index = 1;
        for (String name : command.advisorNames()) {
            // 生成工号：T + 日期序号，如 T001, T002
            String employeeNo = String.format("T%03d", index++);
            // 默认院系设为 "待分配"
            Advisor advisor = new Advisor(name, employeeNo, "待分配");
            advisorRepository.save(advisor);
            advisorIds.add(advisor.getId());
        }

        return CommandResult.ok(advisorIds);
    }
}