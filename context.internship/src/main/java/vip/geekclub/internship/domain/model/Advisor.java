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
 * 指导教师模型
 * 用于管理实习学生的指导教师信息
 */
@Entity
@Table(name = "internship_advisor")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Advisor extends EntitySupport implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 姓名
     */
    @Column(nullable = false)
    private String name;


    /**
     * 创建指导教师
     *
     * @param name       姓名
     * @param employeeNo 工号
     * @param department 所属院系
     */
    public Advisor(String name, String employeeNo, String department) {
        AssertUtil.notNull(name, () -> "姓名不能为空");
        AssertUtil.notNull(employeeNo, () -> "工号不能为空");
        AssertUtil.notNull(department, () -> "所属院系不能为空");

        this.name = name;
    }
}