package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.exception.BusinessLogicException;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.internship.domain.value.SelectionType;

import java.util.HashSet;
import java.util.Set;

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
    @Column(name = "paper_id", nullable = false)
    private Long paperId;

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
     * 选择者集合
     */
    @OneToMany(mappedBy = "thesisSelection", cascade = CascadeType.ALL, orphanRemoval = true)
    private final Set<Selector> selectors = new HashSet<>();

    /**
     * 创建选题记录
     *
     * @param paperId        论文ID
     * @param achievementType 成果形式
     * @param selectionType   选择者类型
     */
    public ThesisSelection(Long paperId, String achievementType, SelectionType selectionType) {
        AssertUtil.notNull(paperId, () -> "论文不能为空");
        AssertUtil.notNull(selectionType, () -> "选择者类型不能为空");

        this.paperId = paperId;
        this.achievementType = achievementType;
        this.selectionType = selectionType;
    }

    /**
     * 添加选择者
     *
     * @param selector 选择者
     */
    public void addSelector(Selector selector) {
        AssertUtil.notNull(selector, () -> "选择者不能为空");

        int currentCount = this.selectors.size();
        int maxCount = this.selectionType.getMaxSelections();

        if (currentCount >= maxCount) {
            throw new BusinessLogicException(
                String.format("当前选择者类型为%s，最多允许%d个选择者，已达到上限",
                    this.selectionType == SelectionType.INDIVIDUAL ? "个人" : "组",
                    maxCount)
            );
        }

        this.selectors.add(selector);
    }

    /**
     * 批量添加选择者
     *
     * @param selectors 选择者集合
     */
    public void addSelectors(Set<Selector> selectors) {
        AssertUtil.notNull(selectors, () -> "选择者集合不能为空");
        AssertUtil.isTrue(!selectors.isEmpty(), () -> "选择者集合不能为空");

        int currentCount = this.selectors.size();
        int newCount = currentCount + selectors.size();
        int maxCount = this.selectionType.getMaxSelections();

        if (newCount > maxCount) {
            throw new BusinessLogicException(
                String.format("当前选择者类型为%s，最多允许%d个选择者，添加后将超过上限",
                    this.selectionType == SelectionType.INDIVIDUAL ? "个人" : "组",
                    maxCount)
            );
        }

        this.selectors.addAll(selectors);
    }

    /**
     * 移除选择者
     *
     * @param selector 选择者
     */
    public void removeSelector(Selector selector) {
        AssertUtil.notNull(selector, () -> "选择者不能为空");
        this.selectors.remove(selector);
    }

    /**
     * 验证选择者数量是否符合要求
     */
    public void validateSelectorCount() {
        int count = this.selectors.size();
        if (!this.selectionType.isValidSelectionCount(count)) {
            throw new BusinessLogicException(
                String.format("当前选择者类型为%s，允许%d-%d个选择者，当前为%d个",
                    this.selectionType == SelectionType.INDIVIDUAL ? "个人" : "组",
                    this.selectionType.getMinSelections(),
                    this.selectionType.getMaxSelections(),
                    count)
            );
        }
    }

    /**
     * 获取选择者数量
     *
     * @return 选择者数量
     */
    public int getSelectorCount() {
        return this.selectors.size();
    }

    /**
     * 检查是否已满
     *
     * @return 是否已满
     */
    public boolean isFull() {
        return this.selectors.size() >= this.selectionType.getMaxSelections();
    }

    /**
     * 更新成果形式
     *
     * @param achievementType 成果形式
     */
    public void updateAchievementType(String achievementType) {
        this.achievementType = achievementType;
    }
}
