package vip.geekclub.security.domain.model;

import jakarta.validation.constraints.Size;
import org.springframework.data.util.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.*;
import lombok.*;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.domain.model.EntitySupport;
import vip.geekclub.framework.utils.ApplicationUtil;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.security.domain.value.CredentialType;

import java.util.Objects;

@Entity
@Table(name = "security_credential")
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Credential extends EntitySupport implements AggregateRoot<Long> {
    private final static Lazy<@NonNull PasswordEncoder> passwordEncoder = Lazy.of(() -> Objects.requireNonNull(ApplicationUtil.getBean(PasswordEncoder.class)));

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long principalId;

    /**
     * 认证类型，例如用户名密码认证、微信认证等
     */
    @Enumerated(EnumType.STRING)
    private CredentialType type;

    /**
     * 标识符，例如用户名或微信 openId
     */
    @Size(min = 1, max = 200, message = "标识符长度必须在1-200个字符之间")
    private String identifier;

    /**
     * 凭证，例如密码或微信 accessToken
     */
    @Size(max = 200, message = "凭证长度必须在1-200个字符之间")
    private String password;

    /**
     * 构造函数
     */
    public Credential(Long principalId, CredentialType type, String identifier, String password) {
        setPrincipalId(principalId);
        setIdentifier(identifier);
        setType(type);

        switch (type) {
            case USERNAME:
            case EMAIL:
            case PHONE:
                setPassword(password);
                break;
            default:
                break;
        }
    }

    /**
     * 设置密码
     */
    private void setPassword(String password) {
        AssertUtil.isTrue(password != null && !password.isBlank(), () -> "密码不能为空");
        this.password = passwordEncoder.get().encode(password);
    }

    /**
     * 变更密码
     */
    public void changePassword(String oldPassword, String newPassword) {
        AssertUtil.isTrue(passwordEncoder.get().matches(oldPassword, this.password), () -> "旧密码错误");
        setPassword(newPassword);
    }
}