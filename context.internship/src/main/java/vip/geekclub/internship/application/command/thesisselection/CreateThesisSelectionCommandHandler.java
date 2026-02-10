package vip.geekclub.internship.application.command.thesisselection;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandContext;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.command.IdResult;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.internship.domain.model.Intern;
import vip.geekclub.internship.domain.model.TeamApplication;
import vip.geekclub.internship.domain.model.ThesisSelection;
import vip.geekclub.internship.domain.repository.InternRepository;
import vip.geekclub.internship.domain.repository.TeamApplicationRepository;
import vip.geekclub.internship.domain.repository.ThesisRepository;
import vip.geekclub.internship.domain.repository.ThesisSelectionRepository;
import vip.geekclub.internship.domain.service.TeamMemberValidator;
import vip.geekclub.internship.domain.value.SelectionType;
import vip.geekclub.internship.domain.value.SelectorValue;

import java.util.List;

@AllArgsConstructor
@Service
public class CreateThesisSelectionCommandHandler implements CommandHandler<CreateThesisSelectionCommand, IdResult> {

    private final ThesisRepository thesisRepository;
    private final ThesisSelectionRepository thesisSelectionRepository;
    private final TeamApplicationRepository teamApplicationRepository;
    private final InternRepository internRepository;
    private final TeamMemberValidator teamMemberValidator;

    @Override
    @Transactional
    public CommandResult<IdResult> execute(CreateThesisSelectionCommand command) {
        // 获取当前用户
        var principal = CommandContext.getCurrentPrincipal();
        var intern = internRepository.findByAuthId(principal.authId())
                .orElseThrow(() -> new ValidationException("当前用户不是实习生"));

        // 验证论文是否存在
        if (!thesisRepository.existsById(command.thesisId())) {
            throw new ValidationException("论文不存在");
        }

        // 准备 选题者 对对ID列表
        List<SelectorValue> studentIds = switch (command.selectionType()) {
            case GROUP -> {
                var ids = command.teamApplication().members().stream()
                        .map(item -> new SelectorValue(item.studentId()))
                        .toList();

                teamMemberValidator.validateMembers(ids, intern);
                yield ids;
            }
            case INDIVIDUAL -> List.of(new SelectorValue(intern.getId()));
        };

        // 更新论文选择人数计数（使用乐观锁）
        if (!thesisRepository.incrementSelectionCount(command.thesisId())) {
            throw new ValidationException("论文选择人数已达上限");
        }

        // 创建并保存论文选题
        ThesisSelection thesisSelection = new ThesisSelection(
                command.thesisId(),
                command.achievementType(),
                command.selectionType(),
                studentIds
        );
        thesisSelectionRepository.save(thesisSelection);

        // 如果是组形式，保存小组申请
        if (command.selectionType() == SelectionType.GROUP) {
            teamApplicationRepository.save(new TeamApplication(
                    thesisSelection.getId(),
                    command.teamApplication()
            ));
        }

        return CommandResult.ok(thesisSelection.getId());
    }
}