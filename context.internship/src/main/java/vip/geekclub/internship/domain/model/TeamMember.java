package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 结组申请小组成员实体
 * 用于管理结组申请中的成员信息
 */
@Entity
@Table(name = "internship_team_member")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamMember {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 结组申请外键ID
     */
    @Column(name = "team_application_id", nullable = false)
    private Long teamApplicationId;

    /**
     * 学生外键ID
     */
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    /**
     * 职责描述（在组中对于该课题的角色和职责）
     */
    @Column(name = "responsibility", nullable = false, length = 500)
    private String responsibility;

    /**
     * 创建小组成员
     *
     * @param teamApplicationId 结组申请外键ID
     * @param studentId         学生外键ID
     * @param responsibility    职责描述
     */
    public TeamMember(Long teamApplicationId, Long studentId, String responsibility) {
        AssertUtil.notNull(teamApplicationId, () -> "结组申请外键ID不能为空");
        AssertUtil.notNull(studentId, () -> "学生外键ID不能为空");
        AssertUtil.notNull(responsibility, () -> "职责描述不能为空");
        AssertUtil.isTrue(!responsibility.trim().isEmpty(), () -> "职责描述不能为空");

        this.teamApplicationId = teamApplicationId;
        this.studentId = studentId;
        this.responsibility = responsibility;
    }
}