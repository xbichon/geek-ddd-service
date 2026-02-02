package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.internship.domain.value.TeamApplicationValue;

import java.util.ArrayList;
import java.util.List;

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
     * 小组成员集合
     */
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "team_application_id")
    private List<TeamMember> members = new ArrayList<>();

    /**
     * 创建结组申请单
     *
     * @param thesisSelectionId 论文选题外键ID
     */
    public TeamApplication(Long thesisSelectionId, TeamApplicationValue application) {

        this.thesisSelectionId = thesisSelectionId;
        this.reason = application.reason();

        this.members = application.members().stream()
                .map(item -> new TeamMember(this,item.studentId(), item.responsibility()))
                .toList();
    }

}