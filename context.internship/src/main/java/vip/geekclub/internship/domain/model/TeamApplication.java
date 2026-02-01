package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 结组申请单模型
 * 用于管理学生论文结组申请信息
 */
@Entity
@Table(name = "internship_team_application")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamApplication implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 论文选题外键ID
     */
    @Column(name = "thesis_selection_id", nullable = false)
    private Long thesisSelectionId;

    /**
     * 结组原因
     */
    @Column(name = "reason", nullable = false, length = 500)
    private String reason;

    /**
     * 创建结组申请单
     *
     * @param thesisSelectionId 论文选题外键ID
     * @param reason            结组原因
     */
    public TeamApplication(Long thesisSelectionId, String reason) {
        AssertUtil.notNull(thesisSelectionId, () -> "论文选题外键ID不能为空");
        AssertUtil.notNull(reason, () -> "结组原因不能为空");
        AssertUtil.isTrue(!reason.trim().isEmpty(), () -> "结组原因不能为空");

        this.thesisSelectionId = thesisSelectionId;
        this.reason = reason;
    }

    /**
     * 更新结组原因
     *
     * @param reason 新的结组原因
     */
    public void updateReason(String reason) {
        AssertUtil.notNull(reason, () -> "结组原因不能为空");
        AssertUtil.isTrue(!reason.trim().isEmpty(), () -> "结组原因不能为空");

        this.reason = reason;
    }
}