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
 * 班级模型
 */
@Entity
@Table(name = "internship_class")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassInfo extends EntitySupport implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 班级名称
     */
    @Column(nullable = false)
    private String name;

    /**
     * 专业ID
     */
    @Column(name = "major_id", nullable = false)
    private Long majorId;

    /**
     * 创建班级
     *
     * @param name    班级名称
     * @param majorId 专业ID
     */
    public ClassInfo(String name, Long majorId) {
        AssertUtil.notNull(name, () -> "班级名称不能为空");
        AssertUtil.notNull(majorId, () -> "专业ID不能为空");
        this.name = name;
        this.majorId = majorId;
    }
}