package vip.geekclub.security.domain.model;

import jakarta.persistence.*;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.domain.model.EntitySupport;
import vip.geekclub.framework.exception.BusinessLogicException;
import vip.geekclub.security.domain.value.Description;
import vip.geekclub.security.domain.value.RoleName;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "security_role")
@Getter @NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends EntitySupport implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户类型
     */
    @NotNull(message = "用户类型不能为空")
    private String userType;

    /**
     * 角色名称
     */
    @Embedded
    @AttributeOverride(name = "name", column = @Column(name = "name"))
    private RoleName roleName = RoleName.empty();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "security_role_permission", joinColumns = @JoinColumn(name = "role_id"))
    @Column(name = "permission_id")
    private final Set<Long> permissionIds = new HashSet<>();

    /**
     * 描述
     */
    @Embedded
    @AttributeOverride(name = "description", column = @Column(name = "description"))
    private Description description = Description.empty();

    /**
     * 是否系统管理员角色（拥有所有权限，不可删除）
     */
    @Column(name = "is_system_admin", nullable = false)
    private boolean systemAdmin = false;

    /**
     * 创建系统管理员角色
     */
    public static Role createSystemAdminRole(String userType) {
        Role role = new Role();
        role.userType = userType;
        role.roleName = RoleName.of("系统管理员");
        role.description = Description.of("拥有所有权限的系统管理员角色");
        role.systemAdmin = true;
        return role;
    }

    /**
     * 删除前校验
     */
    public void validateDeletable() {
        if (this.systemAdmin) {
            throw new BusinessLogicException("系统管理员角色不允许删除");
        }
    }

}
