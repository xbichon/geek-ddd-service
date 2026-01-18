package vip.geekclub.security.auth.domain;

import jakarta.validation.constraints.Size;
import org.springframework.data.util.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;

import jakarta.persistence.*;
import lombok.*;
import vip.geekclub.framework.domain.AggregateRoot;
import vip.geekclub.framework.domain.EntitySupport;
import vip.geekclub.framework.utils.ApplicationUtil;
import vip.geekclub.framework.utils.AssertUtil;
import java.util.Objects;

@Entity
@Table(name = "security_credential")
@Getter
@Setter(AccessLevel.PRIVATE)
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Credential extends EntitySupport implements AggregateRoot<Long> {
    private final static Lazy<PasswordEncoder> passwordEncoder = Lazy.of(() -> Objects.requireNonNull(ApplicationUtil.getBean(PasswordEncoder.class)));

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 关联的用户ID
     */
    private Long userId;

    /**
     * 认证类型，例如用户名密码认证、微信认证等
     */
    @Enumerated(EnumType.STRING)
    private AuthenticationType type;

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

    private Credential(Long userId, AuthenticationType type, String identifier) {
        AssertUtil.notNull(userId, "userId 不能为空");
        AssertUtil.notNull(type, "type 不能为空");

        setUserId(userId);
        setIdentifier(identifier);
        setType(type);
    }

    public static Credential newUsernameAuth(Long userId, String username, String password) {
        // 校验：用户名 <= 20 且非空，密码非空
        AssertUtil.isTrue(username != null && !username.isEmpty(), () -> "用户名不能为空");
        AssertUtil.requireLengthLessThan(username, 21, () -> "用户名不能超过20个字符");

        Credential authCredential = new Credential(userId, AuthenticationType.USERNAME, username);
        authCredential.setPassword(password);
        return authCredential;
    }

    public static Credential newWechatAuth(Long userId, String openId) {
        AssertUtil.notBlank(openId, () -> "openId 不能为空");
        return new Credential(userId, AuthenticationType.WECHAT, openId);
    }

    public static Credential newEmailAuth(Long userId, String email, String password) {
        // 校验：email 合法、密码非空
        AssertUtil.notBlank(email, () -> "邮箱不能为空");
        AssertUtil.isTrue(email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"), () -> "邮箱格式不正确");

        Credential authCredential = new Credential(userId, AuthenticationType.EMAIL, email);
        authCredential.setPassword(password);
        return authCredential;
    }

    public static Credential newPhoneAuth(Long userId, String phone, String password) {
        // 校验：手机合法、密码非空（这里用常见的手机号正则，可按需要调整）
        AssertUtil.notBlank(phone , () -> "手机号不能为空");
        AssertUtil.isTrue(phone.matches("^1\\d{10}$"), () -> "手机号格式不正确");


        Credential authCredential = new Credential(userId, AuthenticationType.PHONE, phone);
        authCredential.setPassword(password);
        return authCredential;
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