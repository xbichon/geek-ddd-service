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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_application_id", nullable = false)
    private TeamApplication teamApplication;

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
     */
    public TeamMember(TeamApplication teamApplication, Long studentId, String responsibility) {

        this.teamApplication = teamApplication;
        this.studentId = studentId;
        this.responsibility = responsibility;
    }
}