package vip.geekclub.security.permission.domain;

import jakarta.persistence.*;

import lombok.*;
import vip.geekclub.framework.domain.AggregateRoot;
import vip.geekclub.framework.domain.EntitySupport;

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

    // =============================== 构造函数 ================================

    public Role(RoleName roleName, Description description) {
        this.roleName = roleName;
        this.description = description;
    }

    // =============================== 业务方法 ================================

    /**
     * 添加权限
     */
    public void addPermission(Long permissionId) {
        this.permissionIds.add(permissionId);
    }

    /**
     * 移除权限
     */
    public void removePermission(Long permissionId) {
        this.permissionIds.remove(permissionId);
    }

    /**
     * 更新角色信息
     */
    public void updateInfo(RoleName roleName, Description description) {
        this.roleName = roleName;
        this.description = description;
    }
}
