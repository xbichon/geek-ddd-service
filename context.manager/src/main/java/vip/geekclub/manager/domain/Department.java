package vip.geekclub.manager.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import vip.geekclub.framework.domain.AggregateRoot;
import vip.geekclub.framework.domain.EntitySupport;
import vip.geekclub.framework.exception.ValidationException;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 部门聚合根
 * 负责管理部门的创建、更新和验证逻辑
 *
 * @author leo
 * @since 1.0
 */
@Entity
@Table(name = "manager_department")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Department extends EntitySupport implements AggregateRoot<Long> {

    // ================================ 常量定义 ================================

    public static final int maxNameLength = 50;
    public static final int maxDescriptionLength = 200;

    private static final int DEPARTMENT_MIN_LEVEL = 1;
    private static final int DEPARTMENT_MAX_LEVEL = 5;

    // ================================ 字段定义 ================================

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 部门名称
     */
    @Size(min = 1, max = 2,message = "名字的长度验证测试")
    private String name;

    /**
     * 父部门ID
     */
    private Long parentId;

    /**
     * 排序号
     */
    @Embedded
    private SortOrder sortOrder = SortOrder.defaultOrder();

    /**
     * 层级信息
     */
    private Integer level;

    /**
     * 部门状态
     */
    @Enumerated(EnumType.STRING)
    private DepartmentStatus status = DepartmentStatus.ENABLED;

    /**
     * 描述
     */
    @Column(length = 200)
    private String description;

    // ================================ 构造函数 ================================

    /**
     * 完整构造函数，用于内部创建Department实例
     */
    private Department(String name, Long parentId, Integer level, SortOrder sortOrder, String description) {
        setName(name);
        setParentId(parentId);
        setLevel(level);
        setSortOrder(sortOrder);
        setStatus(DepartmentStatus.ENABLED);
        setDescription(description);
    }

    // ================================ 业务方法 ================================

    /**
     * 创建部门的静态工厂方法
     * 
     * @param name 部门名称
     * @param parentDepartment 父部门（可为null，表示创建根部门）
     * @param sortOrder 排序顺序
     * @param description 描述信息
     * @return 新创建的部门实例
     */
    public static Department createDepartment(String name, Department parentDepartment, SortOrder sortOrder, String description) {
        // 计算部门层级
        int level = DEPARTMENT_MIN_LEVEL; // 默认为根部门层级
        Long parentId = null;
        
        // 如果指定了父部门
        if (parentDepartment != null) {
            // 验证父部门必须是启用状态
            if (parentDepartment.isDisabled()) {
                throw new ValidationException("父部门已禁用，不能添加子部门");
            }
            
            level = parentDepartment.getLevel() + 1;
            parentId = parentDepartment.getId();
        }
        
        return new Department(
                name,
                parentId,
                level,
                sortOrder,
                description
        );
    }

    /**
     * 更新部门信息
     */
    public void updateDepartment(String name, SortOrder sortOrder, DepartmentStatus status, String description) {
        setName(name);
        setSortOrder(sortOrder);
        setStatus(status);
        setDescription(description);
    }

    /**
     * 验证是否可以删除
     * 根部门不能删除
     */
    public void validateDeletable() {
        if (isRoot()) {
            throw new ValidationException("根部门不能删除");
        }
    }

    /**
     * 验证名称是否变化
     */
    public boolean isChangeName(String newName) {
        return !this.name.equals(newName.trim());
    }

    /**
     * 是否为根部门
     */
    private boolean isRoot() {
        return parentId == null;
    }

    public boolean isDisabled() {
        return status == DepartmentStatus.DISABLED;
    }

    // ================================ 赋值方法 ================================

    /**
     * 设置 部门 名称（带验证）
     */
    private void setName(String name) {
        name = StringUtils.trimToEmpty(name);
        AssertUtil.notEmpty(name, () -> "部门名称不能为空");
        AssertUtil.requireLengthLessThan(name, maxNameLength, () -> "部门名称不能超过" + maxNameLength + "个字符");
        this.name = name;
    }

    /**
     * 设置部门层级
     */
    public void setLevel(int level) {
        AssertUtil.requireNumberBetween(level, DEPARTMENT_MIN_LEVEL, DEPARTMENT_MAX_LEVEL, () -> "部门层级必须在" + DEPARTMENT_MIN_LEVEL + "-" + DEPARTMENT_MAX_LEVEL + "之间");
        this.level = level;
    }

    /**
     * 设置部门状态
     */
    private void setStatus(DepartmentStatus status) {
        AssertUtil.notNull(status, () -> "部门状态不能为空");
        this.status = status;
    }

    /**
     * 设置描述
     */
    private void setDescription(String description) {
        description = StringUtils.trimToEmpty(description);
        AssertUtil.requireLengthLessThan(description, maxDescriptionLength + 1,
                () -> "描述长度不能超过" + maxDescriptionLength + "个字符");
        this.description = description;
    }
}