package vip.geekclub.security.domain;

import org.apache.commons.lang3.StringUtils;
import vip.geekclub.framework.domain.AggregateRoot;
import vip.geekclub.framework.domain.EntitySupport;
import vip.geekclub.framework.utils.AssertUtil;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "security_permission")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Permission extends EntitySupport implements AggregateRoot<Long> {

    // ================================ 常量定义 ================================

    public static final int NAME_MAX_LENGTH = 20;           // 权限名称最大长度
    public static final int DESCRIPTION_MAX_LENGTH = 500;   // 描述最大长度


    // ================================ 字段定义 ================================
    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    /**
     * 权限名称
     */
    @NotBlank(message = "权限名称不能为空")
    @Column(name = "name")
    private String name;

    /**
     * 权限码
     */
    @Embedded
    @AttributeOverride(name = "code", column = @Column(name = "code"))
    private PermissionCode permissionCode = PermissionCode.empty();

    /**
     * 描述
     */
    @Column(name = "description")
    private String description;

    /**
     * 权限组ID
     */
    @Column(name = "permission_group_id")
    private Long permissionGroupId;

    // ============================== 聚合方法 ==================================

    /**
     * 构造函数
     */
    public Permission(String name, PermissionCode code, String description, Long permissionGroupId) {
        setName(name);
        setDescription(description);
        this.permissionCode = code;
        this.permissionGroupId = permissionGroupId;
    }

    /**
     * 更新权限信息（不允许更新权限组ID）
     */
    public void updateInfo(String name, PermissionCode code, String description) {
        setName(name);
        this.permissionCode = code;
        setDescription(description);
    }


    // ============================== 字段赋值 ==================================

    /**
     * 设置权限名称
     */
    private void setName(String name) {
        name = StringUtils.trimToEmpty(name);
        AssertUtil.notBlank(name, () -> "权限名称不能为空");
        AssertUtil.requireLengthBetween(name, 1, NAME_MAX_LENGTH, () -> "权限名称长度必须在1-" + NAME_MAX_LENGTH + "之间");
        this.name = name;
    }

    /**
     * 设置描述
     */
    private void setDescription(String description) {
        description = StringUtils.trimToEmpty(description);
        AssertUtil.requireLengthLessThan(description, DESCRIPTION_MAX_LENGTH + 1, () -> "描述长度不能超过" + DESCRIPTION_MAX_LENGTH + "个字符");
        this.description = description;
    }

}