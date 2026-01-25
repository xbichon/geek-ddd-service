package vip.geekclub.security.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.domain.model.EntitySupport;
import vip.geekclub.framework.exception.BusinessLogicException;
import vip.geekclub.framework.utils.AssertUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "security_principal")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Principal extends EntitySupport implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "用户类型不能为空")
    private String appType;

    @Column(name = "auth_id")
    private UUID authId;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "security_principal_role", joinColumns = @JoinColumn(name = "principal_id"))
    @Column(name = "role_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private final Set<Long> roleIds = new HashSet<>();

    public Principal(String appType) {
        this(appType, null);
    }

    public Principal(String appType, UUID authId) {
        this.appType = appType;
        this.authId = authId;
        AssertUtil.notNull(appType, () -> "应用类型不能为空");
    }

    public Principal(String appType, UUID authId, Set<Long> roleIds) {
        AssertUtil.notNull(appType, () -> "应用类型不能为空");
        this.appType = appType;
        this.authId = authId;

        if (roleIds != null && !roleIds.isEmpty()) {
            if (roleIds.contains(-1L)) {
                this.roleIds.add(-1L);
            } else {
                this.roleIds.addAll(roleIds);
            }
        }
    }

    /**
     * 更新角色
     */
    public void updateRole(Set<Long> roleIds) {
        if (this.roleIds.contains(-1L)) {
            throw new BusinessLogicException("超级管理员不能修改角色");
        }

        // 移除不再拥有的角色
        this.roleIds.retainAll(roleIds);
        // 增加新的角色
        this.roleIds.addAll(roleIds);
    }

    /**
     * 新建超级管理员(Teacher)
     */
    public static Principal newAdmin(String userType) {
        Principal principal = new Principal(userType);
        principal.roleIds.add(-1L);
        return principal;
    }
}