package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.internship.domain.value.SelectionType;
import vip.geekclub.internship.domain.value.SelectorValue;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 选题记录模型
 * 用于管理学生或小组对论文的选题记录
 */
@Entity
@Table(name = "internship_thesis_selection")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThesisSelection implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 论文ID
     */
    @NotNull(message = "论文不能为空")
    @Column(name = "thesis_id", nullable = false)
    private Long thesisId;

    /**
     * 成果形式
     */
    @Column(name = "achievement_type", nullable = false)
    private String achievementType;

    /**
     * 选择者类型
     */
    @NotNull(message = "选择者类型不能为空")
    @Enumerated(EnumType.STRING)
    @Column(name = "selection_type", nullable = false)
    private SelectionType selectionType;

    /**
     * 创建者ID（选题记录创建人的实习生ID）
     */
    @NotNull(message = "创建者ID不能为空")
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;

    /**
     * 选择者集合
     */
    @OneToMany(mappedBy = "thesisSelection", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Selector> selectors = new HashSet<>();

    /**
     * 创建选题记录（带学生ID列表）
     *
     * @param thesisId        论文ID
     * @param achievementType 成果形式
     * @param selectionType   选择者类型
     * @param creatorId       创建者ID（选题记录创建人的实习生ID）
     * @param studentIds      学生ID列表
     */
    public ThesisSelection(Long thesisId, String achievementType, SelectionType selectionType, Long creatorId, List<SelectorValue> studentIds) {

        AssertUtil.notNull(thesisId, () -> "论文不能为空");
        AssertUtil.notNull(selectionType, () -> "选择者类型不能为空");
        AssertUtil.notNull(creatorId, () -> "创建者ID不能为空");

        this.thesisId = thesisId;
        this.achievementType = achievementType;
        this.selectionType = selectionType;
        this.creatorId = creatorId;

        // 验证并创建选择者
        validateSelectorsCount(studentIds);

        // 创建选择者
        this.selectors.addAll(
                studentIds.stream()
                        .map(item -> new Selector(item.studentId(), this))
                        .collect(Collectors.toSet())
        );
    }

    /**
     * 验证学生数量并创建选择者
     *
     * @param studentIds 学生ID列表
     */
    private void validateSelectorsCount(List<SelectorValue> studentIds) {
        if (studentIds == null || studentIds.isEmpty()) {
            throw new BusinessException("选择者列表不能为空");
        }

        int count = studentIds.size();
        if (this.selectionType == SelectionType.INDIVIDUAL) {
            // 个人形式：成员数只能是1个
            if (studentIds.size() != 1) {
                throw new BusinessException("个人选题形式只能选择1个学生");
            }
        } else {
            // 小组形式：成员数2-5个
            if (count < 2 || count > 5) {
                throw new BusinessException("小组选题形式成员数量必须在2-5人之间");
            }
        }
    }
}
