package vip.geekclub.security.auth.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;
import vip.geekclub.common.domain.AggregateRoot;
import vip.geekclub.common.domain.EntitySupport;
import vip.geekclub.common.exception.BusinessLogicException;
import vip.geekclub.common.utils.AssertUtil;
import vip.geekclub.security.auth.common.UserType;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "security_user")
@Getter @Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserPrincipal extends EntitySupport implements AggregateRoot<Long> {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "用户类型不能为空")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "security_user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role_id")
    private final Set<Long> roleIds = new HashSet<>();

    /**
     * 是否是超级管理员
     */
    private Boolean isSuperAdmin = false;

    public UserPrincipal(UserType userType) {
        this.userType = userType;
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
    public static UserPrincipal newTeacherAdmin() {
        UserPrincipal userPrincipal = new UserPrincipal(UserType.TEACHER);
        userPrincipal.isSuperAdmin = true;
        return userPrincipal;
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