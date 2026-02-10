package vip.geekclub.internship.application.command.thesisselection;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.CommandContext;
import vip.geekclub.framework.command.CommandHandler;
import vip.geekclub.framework.command.CommandResult;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.internship.domain.model.Intern;
import vip.geekclub.internship.domain.repository.InternRepository;
import vip.geekclub.internship.domain.service.TeamMemberValidator;
import vip.geekclub.internship.domain.service.ThesisSelectionDomainService;
import vip.geekclub.internship.domain.value.SelectionType;
import vip.geekclub.internship.domain.value.SelectorValue;

import java.util.List;

@AllArgsConstructor
@Service
public class CreateThesisSelectionCommandHandler implements CommandHandler<CreateThesisSelectionCommand, Void> {

    private final InternRepository internRepository;
    private final TeamMemberValidator teamMemberValidator;
    private final ThesisSelectionDomainService domainService;

    @Override
    @Transactional
    public CommandResult<Void> execute(CreateThesisSelectionCommand command) {
        // 1. 获取当前用户
        var principal = CommandContext.getCurrentPrincipal();
        var intern = internRepository.findByAuthId(principal.authId())
                .orElseThrow(() -> new ValidationException("当前用户不是实习生"));

        // 2. 准备选题者列表
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

        // 3. 调用领域服务执行选题
        domainService.selectThesis(command.thesisId(), command.achievementType(), command.selectionType(),
                studentIds, command.teamApplication());

        return CommandResult.ok();
    }
}