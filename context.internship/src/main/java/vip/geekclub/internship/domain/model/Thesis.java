package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.internship.domain.value.AchievementType;

import java.util.HashSet;
import java.util.Set;

/**
 * 论文模型
 * 用于管理实习论文的基本信息，包括标题、选课上限和当前选课人数
 */
@Entity
@Table(name = "internship_thesis")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Thesis implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 论文标题
     */
    @Column(nullable = false)
    private String title;

    /**
     * 可选上限
     */
    @Column(name = "max_selections", nullable = false)
    private Integer maxSelections;

    /**
     * 已选人数
     */
    @Column(name = "current_selections", nullable = false)
    private Integer currentSelections = 0;

    /**
     * 成果形式集合
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "internship_thesis_achievement_type",
        joinColumns = @JoinColumn(name = "thesis_id")
    )
    @Column(name = "type", length = 50)
    private final Set<AchievementType> achievementTypes = new HashSet<>();

    /**
     * 创建论文
     *
     * @param title         论文标题
     * @param maxSelections 可选上限
     */
    public Thesis(String title, Integer maxSelections) {
        AssertUtil.notNull(title, () -> "论文标题不能为空");
        AssertUtil.notNull(maxSelections, () -> "可选上限不能为空");
        AssertUtil.isTrue(maxSelections > 0, () -> "可选上限必须大于0");

        this.title = title;
        this.maxSelections = maxSelections;
        this.currentSelections = 0;
    }

    /**
     * 增加选课人数
     */
    public void incrementSelections() {
        if (this.currentSelections >= this.maxSelections) {
            throw new IllegalStateException("论文选课人数已达上限");
        }
        this.currentSelections++;
    }

    /**
     * 减少选课人数
     */
    public void decrementSelections() {
        if (this.currentSelections <= 0) {
            throw new IllegalStateException("论文选课人数不能小于0");
        }
        this.currentSelections--;
    }

    /**
     * 添加成果形式
     *
     * @param achievementType 成果形式
     */
    public void addAchievementType(AchievementType achievementType) {
        AssertUtil.notNull(achievementType, () -> "成果形式不能为空");
        this.achievementTypes.add(achievementType);
    }

    /**
     * 批量添加成果形式
     *
     * @param achievementTypes 成果形式集合
     */
    public void addAchievementTypes(Set<AchievementType> achievementTypes) {
        AssertUtil.notNull(achievementTypes, () -> "成果形式集合不能为空");
        AssertUtil.isTrue(!achievementTypes.isEmpty(), () -> "成果形式集合不能为空");
        this.achievementTypes.addAll(achievementTypes);
    }

    /**
     * 移除成果形式
     *
     * @param achievementType 成果形式
     */
    public void removeAchievementType(AchievementType achievementType) {
        AssertUtil.notNull(achievementType, () -> "成果形式不能为空");
        this.achievementTypes.remove(achievementType);
    }

    /**
     * 清空所有成果形式
     */
    public void clearAchievementTypes() {
        this.achievementTypes.clear();
    }

    /**
     * 获取成果形式数量
     *
     * @return 成果形式数量
     */
    public int getAchievementTypeCount() {
        return this.achievementTypes.size();
    }
}
