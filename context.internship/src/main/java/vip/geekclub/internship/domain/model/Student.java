package vip.geekclub.internship.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.domain.model.EntitySupport;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 学生模型
 * 用于管理实习学生的基本信息，包括姓名、学号、年级、专业、班级和指导教师
 */
@Entity
@Table(name = "internship_student")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student extends EntitySupport implements AggregateRoot<Long> {

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
     * 年级
     */
    @Column(nullable = false)
    private String grade;

    /**
     * 专业
     */
    @Column(nullable = false)
    private String major;

    /**
     * 班级
     */
    @Column(nullable = false)
    private String className;

    /**
     * 指导教师ID
     */
    @Column(name = "advisor_id")
    private Long advisorId;

    /**
     * 创建学生
     *
     * @param name       姓名
     * @param studentNo  学号
     * @param grade      年级
     * @param major      专业
     * @param className  班级
     * @param advisorId  指导教师ID
     */
    public Student(String name, String studentNo, String grade, String major, String className, Long advisorId) {
        AssertUtil.notNull(name, () -> "姓名不能为空");
        AssertUtil.notNull(studentNo, () -> "学号不能为空");
        AssertUtil.notNull(grade, () -> "年级不能为空");
        AssertUtil.notNull(major, () -> "专业不能为空");
        AssertUtil.notNull(className, () -> "班级不能为空");

        this.name = name;
        this.studentNo = studentNo;
        this.grade = grade;
        this.major = major;
        this.className = className;
        this.advisorId = advisorId;
    }

}
