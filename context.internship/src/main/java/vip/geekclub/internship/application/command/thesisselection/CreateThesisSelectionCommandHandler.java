package vip.geekclub.internship.application.command.thesisselection;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.geekclub.framework.command.VoidCommandHandler;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.internship.domain.repository.InternRepository;
import vip.geekclub.internship.domain.service.TeamMemberValidator;
import vip.geekclub.internship.domain.service.ThesisSelectionDomainService;
import vip.geekclub.internship.domain.value.SelectorValue;

import java.util.List;

@AllArgsConstructor
@Service
public class CreateThesisSelectionCommandHandler implements VoidCommandHandler<CreateThesisSelectionCommand> {

    private final InternRepository internRepository;
    private final TeamMemberValidator teamMemberValidator;
    private final ThesisSelectionDomainService domainService;

    @Override
    @Transactional
    public void executeVoid(CreateThesisSelectionCommand command) {
        // 1. 根据创建者ID获取实习生信息
        var intern = internRepository.findById(command.creatorId())
                .orElseThrow(() -> new NotFoundException("创建者不存在"));

        // 2. 准备选题者列表，根据选择的是组队还是个人，做不同的处理
        List<SelectorValue> studentIds = switch (command.selectionType()) {
            case GROUP -> {
                var ids = command.teamApplication().members().stream()
                        .map(item -> new SelectorValue(item.studentId()))
                        .toList();

                // 验证组队成员资格
                teamMemberValidator.validateMembers(ids, intern);
                yield ids;
            }
            case INDIVIDUAL -> List.of(new SelectorValue(intern.getId()));
        };

        // 3. 调用领域服务执行选题
        domainService.selectThesis(command.thesisId(), command.achievementType(), command.selectionType(),
                command.creatorId(), studentIds, command.teamApplication());

    }
}