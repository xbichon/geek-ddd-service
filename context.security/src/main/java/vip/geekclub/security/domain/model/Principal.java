package vip.geekclub.security.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.domain.model.EntitySupport;
import vip.geekclub.framework.exception.BusinessLogicException;
import vip.geekclub.framework.utils.AssertUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "security_principal")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Principal extends EntitySupport implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "用户类型不能为空")
    private String userType;

    @Column(name = "external_uuid")
    private UUID externalUuid;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "security_user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    private final Set<Long> roleIds = new HashSet<>();

    /**
     * 是否是超级管理员
     */
    private Boolean isSuperAdmin = false;

    public Principal(String userType) {
        this(userType, null);
    }

    public Principal(String userType, UUID externalUuid) {
        this.userType = userType;
        this.externalUuid = externalUuid;
        AssertUtil.notNull(userType, () -> "用户类型不能为空");
    }

    public void updateRole(Set<Long> roleIds) {
        // 移除不再拥有的角色
        this.roleIds.retainAll(roleIds);
        // 增加新的角色
        this.roleIds.addAll(roleIds);
    }

    public void clearRole() {
        this.roleIds.clear();
    }

    /**
     * 新建超级管理员(Teacher)
     */
    public static Principal newAdmin(String userType) {
        Principal principal = new Principal(userType);
        principal.isSuperAdmin = true;
        return principal;
    }

    /**
     * 是否可以删除
     */
    public void validateDeletable() {
        if (isSuperAdmin) {
            throw new BusinessLogicException("超级管理员不能删除");
        }
    }
}