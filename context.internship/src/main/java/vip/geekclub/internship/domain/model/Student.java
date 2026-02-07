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
 * 学生模型
 * 用于管理实习学生的基本信息，包括姓名、学号、班级和指导教师
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
     * 班级ID
     */
    @Column(name = "class_id", nullable = false)
    private Long classId;

    /**
     * 指导教师ID
     */
    @Column(name = "advisor_id")
    private Long advisorId;

    /**
     * 创建学生
     *
     * @param name      姓名
     * @param studentNo 学号
     * @param classId   班级ID
     * @param advisorId 指导教师ID
     */
    public Student(String name, String studentNo, Long classId, Long advisorId) {
        AssertUtil.notNull(name, () -> "姓名不能为空");
        AssertUtil.notNull(studentNo, () -> "学号不能为空");
        AssertUtil.notNull(classId, () -> "班级ID不能为空");

        this.name = name;
        this.studentNo = studentNo;
        this.classId = classId;
        this.advisorId = advisorId;
    }
}