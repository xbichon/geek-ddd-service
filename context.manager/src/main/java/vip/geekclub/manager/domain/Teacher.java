package vip.geekclub.manager.domain;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import vip.geekclub.framework.domain.AggregateRoot;
import vip.geekclub.framework.domain.EntitySupport;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 教师聚合根
 * 负责管理教师的创建、更新和验证逻辑
 *
 * @author leo
 * @since 1.0
 */
@Entity
@Table(name = "manager_teacher")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher extends EntitySupport implements AggregateRoot<Long> {

    // ================================ 常量定义 ================================

    public static final int maxNameLength = 50;
    public static final int maxPhoneLength = 20;
    public static final int maxEmailLength = 100;
    public static final int maxRemarkLength = 200;

    // ================================ 字段定义 ================================

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 教师姓名
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 所属部门ID
     */
    private Long departmentId;


    /**
     * 教师状态
     */
    @Enumerated(EnumType.STRING)
    private TeacherStatus status = TeacherStatus.ENABLED;

    /**
     * 备注
     */
    @Column(length = 200)
    private String remark;

    // ================================ 构造函数 ================================

    /**
     * 完整构造函数，用于内部创建Teacher实例
     */
    private Teacher(String name, String phone, String email, Long departmentId, String remark) {
        setName(name);
        setPhone(phone);
        setEmail(email);
        setDepartmentId(departmentId);
        setStatus(TeacherStatus.ENABLED);
        setRemark(remark);
    }

    // ================================ 业务方法 ================================

    /**
     * 创建教师的静态工厂方法
     *
     * @param name 教师姓名
     * @param phone 手机号
     * @param email 邮箱
     * @param departmentId 所属部门ID
     * @param remark 备注信息
     * @return 新创建的教师实例
     */
    public static Teacher createTeacher(String name, String phone, String email, Long departmentId, String remark) {
        return new Teacher(
                name,
                phone,
                email,
                departmentId,
                remark
        );
    }

    /**
     * 更新教师信息
     */
    public void updateTeacher(String name, String phone, String email, Long departmentId, TeacherStatus status, String remark) {
        setName(name);
        setPhone(phone);
        setEmail(email);
        setDepartmentId(departmentId);
        setStatus(status);
        setRemark(remark);
    }

    /**
     * 验证名称是否变化
     */
    public boolean isChangeName(String newName) {
        return !this.name.equals(newName.trim());
    }


    /**
     * 验证教师是否可删除
     */
    public void validateDeletable() {
        AssertUtil.isTrue(status == TeacherStatus.ENABLED, () -> "只有启用状态的教师才能被删除");
    }

    // ================================ 赋值方法 ================================

    /**
     * 设置教师姓名（带验证）
     */
    private void setName(String name) {
        name = StringUtils.trimToEmpty(name);
        AssertUtil.notEmpty(name, () -> "教师姓名不能为空");
        AssertUtil.requireLengthLessThan(name, maxNameLength, () -> "教师姓名不能超过" + maxNameLength + "个字符");
        this.name = name;
    }

    /**
     * 设置手机号（带验证）
     */
    private void setPhone(String phone) {
        phone = StringUtils.trimToEmpty(phone);
        AssertUtil.requireLengthLessThan(phone, maxPhoneLength, () -> "手机号不能超过" + maxPhoneLength + "个字符");
        this.phone = phone;
    }

    /**
     * 设置邮箱（带验证）
     */
    private void setEmail(String email) {
        email = StringUtils.trimToEmpty(email);
        AssertUtil.requireLengthLessThan(email, maxEmailLength, () -> "邮箱不能超过" + maxEmailLength + "个字符");
        this.email = email;
    }

    /**
     * 设置所属部门ID
     */
    private void setDepartmentId(Long departmentId) {
        AssertUtil.notNull(departmentId, () -> "所属部门不能为空");
        this.departmentId = departmentId;
    }

    /**
     * 设置教师状态
     */
    private void setStatus(TeacherStatus status) {
        AssertUtil.notNull(status, () -> "教师状态不能为空");
        this.status = status;
    }

    /**
     * 设置备注
     */
    private void setRemark(String remark) {
        remark = StringUtils.trimToEmpty(remark);
        AssertUtil.requireLengthLessThan(remark, maxRemarkLength + 1,
                () -> "备注长度不能超过" + maxRemarkLength + "个字符");
        this.remark = remark;
    }
}