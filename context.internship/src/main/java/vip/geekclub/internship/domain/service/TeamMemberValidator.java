package vip.geekclub.internship.domain.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.internship.domain.model.Intern;
import vip.geekclub.internship.domain.repository.InternRepository;
import vip.geekclub.internship.domain.value.SelectorValue;

import java.util.List;
import java.util.Objects;

/**
 * 小组成员验证器
 * 负责验证结组申请的小组成员资格
 */
@AllArgsConstructor
@Service
public class TeamMemberValidator {

    private final InternRepository internRepository;

    /**
     * 验证小组成员
     *
     * @param studentIds  选择者值列表
     * @param currentUser 当前用户（实习生）
     */
    public void validateMembers(List<SelectorValue> studentIds, Intern currentUser) {
        // 转换为成员ID列表
        List<Long> memberIds = studentIds.stream()
                .map(SelectorValue::studentId)
                .toList();

        // 验证当前用户在组员中
        if (!memberIds.contains(currentUser.getId())) {
            throw new ValidationException("当前用户不是小组成员");
        }

        // 查询所有组员信息
        List<Intern> members = internRepository.findAllByIdIn(memberIds);

        // 验证所有组员都存在
        if (members.size() != memberIds.size()) {
            throw new ValidationException("部分小组成员不存在");
        }

        // 获取当前用户的指导老师
        String currentAdvisor = currentUser.getAdvisorName();

        // 验证所有组员的指导老师与当前用户的指导老师相同
        for (Intern member : members) {
            if (!Objects.equals(member.getAdvisorName(), currentAdvisor)) {
                throw new ValidationException("小组成员的指导老师必须相同");
            }
        }
    }
}