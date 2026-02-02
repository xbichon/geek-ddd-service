package vip.geekclub.internship.application.command.thesisselection;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.command.IdResult;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.internship.domain.model.TeamApplication;
import vip.geekclub.internship.domain.model.ThesisSelection;
import vip.geekclub.internship.domain.repository.TeamApplicationRepository;
import vip.geekclub.internship.domain.repository.ThesisRepository;
import vip.geekclub.internship.domain.repository.ThesisSelectionRepository;
import vip.geekclub.internship.domain.value.SelectionType;
import vip.geekclub.internship.domain.value.SelectorValue;

import java.util.List;

@AllArgsConstructor
@Service
public class CreateThesisSelectionCommandHandler implements CommandHandler<CreateThesisSelectionCommand, IdResult> {

    private final ThesisRepository thesisRepository;
    private final ThesisSelectionRepository thesisSelectionRepository;
    private final TeamApplicationRepository teamApplicationRepository;

    @Override
    @Transactional
    public CommandResult<IdResult> execute(CreateThesisSelectionCommand command) {
        // 准备学生ID列表
        List<SelectorValue> studentIds = command.selectionType() == SelectionType.GROUP
                ? command.teamApplication().members().stream()
                .map(item -> new SelectorValue(item.studentId()))
                .toList()
                : List.of();

        // 创建论文选题实体（构造函数中完成验证和选择者创建）
        ThesisSelection thesisSelection = new ThesisSelection(
                command.thesisId(),
                command.achievementType(),
                command.selectionType(),
                studentIds
        );

        // 保存实体
        // 1. 保存论文选题
        thesisSelectionRepository.save(thesisSelection);

        // 2. 更新论文选择人数计数（使用SQL方式）
        int updatedCount = thesisRepository.incrementSelectionCount(command.thesisId());
        if (updatedCount == 0) {
            throw new ValidationException("论文选择人数已达上限");
        }

        // 3. 创建并保存小组成员（如果是组形式）
        if (command.selectionType() == SelectionType.GROUP) {

            // 更新结组申请单的选题ID
            var teamApplication = new TeamApplication(
                    thesisSelection.getId(),
                    command.teamApplication()
            );
            teamApplicationRepository.save(teamApplication);
        }

        return CommandResult.ok(thesisSelection.getId());
    }
}