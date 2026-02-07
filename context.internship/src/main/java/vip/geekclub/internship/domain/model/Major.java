package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.domain.model.EntitySupport;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 专业模型
 */
@Entity
@Table(name = "internship_major")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Major extends EntitySupport implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 专业名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 年级ID
     */
    @Column(name = "grade_id", nullable = false)
    private Long gradeId;

    /**
     * 创建专业
     *
     * @param name    专业名称
     * @param gradeId 年级ID
     */
    public Major(String name, Long gradeId) {
        AssertUtil.notNull(name, () -> "专业名称不能为空");
        AssertUtil.notNull(gradeId, () -> "年级ID不能为空");
        this.name = name;
        this.gradeId = gradeId;
    }
}