package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 年级模型
 */
@Entity
@Table(name = "internship_grade")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 年级名称
     */
    @Column(nullable = false, unique = true)
    private String name;

    /**
     * 创建年级
     *
     * @param name 年级名称
     */
    public Grade(String name) {
        AssertUtil.notNull(name, () -> "年级名称不能为空");
        this.name = name;
    }
}