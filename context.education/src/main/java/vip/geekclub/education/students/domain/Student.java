package vip.geekclub.education.students.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import vip.geekclub.framework.domain.AggregateRoot;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.education.students.common.Sex;

@Entity()
@Table(name = "student_student")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student implements AggregateRoot<Long> {

    public static final int MAX_NAME_LENGTH = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 告知主键生成策略
    private Long id;

    @Column(name = "xm")
    private String name;

    private Sex sex;

    public Student(String name, Sex sex) {
        setName(name);
        setSex(sex);
    }

    public void setName(String name) {
        AssertUtil.notEmpty(name, "姓名不能为空");
        AssertUtil.requireLengthLessThan(name, MAX_NAME_LENGTH, () -> "姓名长度不能超过" + MAX_NAME_LENGTH);

        this.name = StringUtils.trim(name);
    }


}
