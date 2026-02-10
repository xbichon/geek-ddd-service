package vip.geekclub.internship.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.internship.domain.model.TeamApplication;
import vip.geekclub.internship.domain.model.ThesisSelection;
import vip.geekclub.internship.domain.repository.TeamApplicationRepository;
import vip.geekclub.internship.domain.repository.ThesisRepository;
import vip.geekclub.internship.domain.repository.ThesisSelectionRepository;
import vip.geekclub.internship.domain.value.SelectionType;
import vip.geekclub.internship.domain.value.SelectorValue;
import vip.geekclub.internship.domain.value.TeamApplicationValue;

import java.util.List;

/**
 * 选题领域服务
 * 封装选题的核心业务规则
 */
@Component
@AllArgsConstructor
public class ThesisSelectionDomainService {

    private final ThesisRepository thesisRepository;
    private final ThesisSelectionRepository thesisSelectionRepository;
    private final TeamApplicationRepository teamApplicationRepository;

    /**
     * 执行选题
     *
     * @param thesisId        论文ID
     * @param achievementType 成果形式
     * @param selectionType   选择类型（个人/组）
     * @param studentIds      选题者列表
     */
    public void selectThesis(Long thesisId, String achievementType, SelectionType selectionType,
                             List<SelectorValue> studentIds, TeamApplicationValue teamApplicationValue) {
        // 1. 业务规则：检查论文是否存在
        if (!thesisRepository.existsById(thesisId)) {
            throw new ValidationException("论文不存在");
        }

        // 2. 业务规则：检查论文是否已满（使用乐观锁）
        if (!thesisRepository.incrementSelectionCount(thesisId)) {
            throw new ValidationException("论文选择人数已达上限");
        }

        // 3. 创建并保存选题实体
        ThesisSelection thesisSelection = new ThesisSelection(
                thesisId,
                achievementType,
                selectionType,
                studentIds
        );
        thesisSelectionRepository.save(thesisSelection);

        // 4. 如果是组形式，保存小组申请
        if (selectionType == SelectionType.GROUP && teamApplicationValue != null) {
            TeamApplication teamApplication = new TeamApplication(
                    thesisSelection.getId(),
                    teamApplicationValue
            );
            teamApplicationRepository.save(teamApplication);
        }
    }
}