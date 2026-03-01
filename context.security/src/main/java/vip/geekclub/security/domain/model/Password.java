package vip.geekclub.security.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.util.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.domain.model.EntitySupport;
import vip.geekclub.framework.exception.InvalidCredentialsException;
import vip.geekclub.framework.utils.ApplicationUtil;
import vip.geekclub.framework.utils.AssertUtil;

import java.util.Objects;

@Entity
@Table(name = "security_password")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Password extends EntitySupport implements AggregateRoot<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long principalId;

    private String authId;
    private static final Lazy<@NonNull PasswordEncoder> passwordEncoder = Lazy.of(() ->
            Objects.requireNonNull(ApplicationUtil.getBean(PasswordEncoder.class))
    );

    /**
     * 加密后的密码哈希
     */
    @Size(max = 200, message = "密码哈希长度不能超过200个字符")
    private String hash;

    /**
     * 私有构造函数
     */
    private Password(Long principalId, String authId, String password) {
        setPrincipalId(principalId);
        setAuthId(authId);
        setHash(password);
    }

    /**
     * 创建密码凭证
     */
    public static Password create(Long principalId, String authId, String password) {
        return new Password(principalId, authId, password);
    }

    /**
     * 设置密码哈希（加密存储）
     */
    private void setHash(String password) {
        AssertUtil.isTrue(password != null && !password.isBlank(), () -> "密码不能为空");
        this.hash = passwordEncoder.get().encode(password);
    }


    /**
     * 验证密码
     */
    public void verifyPassword(String rawPassword) {
        if (!passwordEncoder.get().matches(rawPassword, this.hash)) {
            throw new InvalidCredentialsException("密码错误");
        }
    }
}