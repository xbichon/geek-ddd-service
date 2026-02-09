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
 * 实习生模型
 * 用于管理实习学生的基本信息，包括姓名、学号、班级和指导教师
 */
@Entity
@Table(name = "internship_intern")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Intern extends EntitySupport implements AggregateRoot<Long> {

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
     * 学号
     */
    @Column(nullable = false, unique = true)
    private String studentNo;

    /**
     * 班级名称
     */
    @Column(name = "class_name", nullable = false, length = 100)
    private String className;

    /**
     * 指导教师姓名
     */
    @Column(name = "advisor_name", length = 100)
    private String advisorName;

    /**
     * 创建实习生
     *
     * @param name        姓名
     * @param studentNo   学号
     * @param className   班级名称
     * @param advisorName 指导教师姓名
     */
    public Intern(String name, String studentNo, String className, String advisorName) {
        AssertUtil.notNull(name, () -> "姓名不能为空");
        AssertUtil.notNull(studentNo, () -> "学号不能为空");
        AssertUtil.notNull(className, () -> "班级名称不能为空");

        this.name = name;
        this.studentNo = studentNo;
        this.className = className;
        this.advisorName = advisorName;
    }
}