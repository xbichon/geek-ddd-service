package vip.geekclub.security.domain;

import vip.geekclub.framework.domain.AggregateRoot;
import vip.geekclub.framework.domain.EntitySupport;
import vip.geekclub.framework.utils.StringUtil;
import vip.geekclub.framework.utils.AssertUtil;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "security_permission_group")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PermissionGroup extends EntitySupport implements AggregateRoot<Long> {

    // ================================ 常量定义 ================================

    /**
     * 权限组名称最大长度
     */
    public static final int NAME_MAX_LENGTH = 100;

    /**
     * 描述最大长度
     */
    public static final int DESCRIPTION_MAX_LENGTH = 500;

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    // ================================ 字段定义 ================================

    /**
     * 权限组名称
     */
    @NotBlank(message = "权限组名称不能为空")
    @Size(min = 1, max = NAME_MAX_LENGTH, message = "权限组名称长度必须在1-" + NAME_MAX_LENGTH + "之间")
    @Column(name = "name")
    private String name;

    /**
     * 权限组描述
     */
    @Size(max = DESCRIPTION_MAX_LENGTH, message = "描述长度不能超过" + DESCRIPTION_MAX_LENGTH + "个字符")
    @Column(name = "description")
    private String description;

    /**
     * 排序字段
     */
    @Embedded
    @AttributeOverride(name = "sortOrder", column = @Column(name = "sort_order"))
    private SortOrder sortOrder = SortOrder.defaultOrder();

    // =============================== 构造函数 ================================

    /**
     * 创建权限组
     */
    public static PermissionGroup createPermissionGroup(String name, String description, SortOrder sortOrder) {
        return new PermissionGroup(name, description, sortOrder);
    }

    private PermissionGroup(String name, String description, SortOrder sortOrder) {
        // 清除空格
        name = StringUtil.trimToNull(name);
        description = StringUtil.trimToNull(description);

        // 校验参数
        AssertUtil.notBlank(name, () -> "权限组名称不能为空");
        AssertUtil.requireLengthBetween(name, 1, NAME_MAX_LENGTH, () -> "权限组名称长度必须在1-" + NAME_MAX_LENGTH + "之间");
        AssertUtil.requireLengthLessThan(description, DESCRIPTION_MAX_LENGTH + 1, () -> "描述长度不能超过" + DESCRIPTION_MAX_LENGTH + "个字符");

        // 设置字段值
        setName(name);
        setDescription(description);
        this.sortOrder = sortOrder;
    }

    // =============================== 业务方法 ================================

    /**
     * 更新权限组信息
     */
    public void updatePermissionGroup(String name, String description, SortOrder sortOrder) {
        setName(name);
        setDescription(description);
        this.sortOrder = sortOrder;
    }

    public void setName(String name) {

        name = StringUtil.trimToNull(name);
        AssertUtil.notBlank(name, () -> "权限组名称不能为空");
        AssertUtil.requireLengthBetween(name, 1, NAME_MAX_LENGTH, () -> "权限组名称长度必须在1-" + NAME_MAX_LENGTH + "之间");

        this.name = name;
    }

    public void setDescription(String description) {
        description = StringUtil.trimToNull(description);
        AssertUtil.requireLengthLessThan(description, DESCRIPTION_MAX_LENGTH + 1, () -> "描述长度不能超过" + DESCRIPTION_MAX_LENGTH + "个字符");
        this.description = description;
    }
}