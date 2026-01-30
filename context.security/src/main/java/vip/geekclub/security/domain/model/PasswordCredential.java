package vip.geekclub.security.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.util.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import vip.geekclub.framework.exception.InvalidCredentialsException;
import vip.geekclub.framework.utils.ApplicationUtil;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.security.domain.value.IdentifierValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "security_credential_password")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordCredential extends Credential {
    private static final Lazy<@NonNull PasswordEncoder> passwordEncoder = Lazy.of(() ->
            Objects.requireNonNull(ApplicationUtil.getBean(PasswordEncoder.class))
    );

    /**
     * 标识符列表，一个用户可以有多个标识符（用户名、邮箱、电话等）
     */
    private List<Identifier> identifier = new ArrayList<>();

    /**
     * 加密后的密码
     */
    @Size(max = 200, message = "密码长度不能超过200个字符")
    private String password;

    /**
     * 私有构造函数
     */
    private PasswordCredential(Long principalId, List<IdentifierValue> identifierValues, String password, String userType) {
        setPrincipalId(principalId);
        setPassword(password);
        setIdentifier(identifierValues, userType);
    }

    /**
     * 创建包含多个标识符的密码凭证
     */
    public static PasswordCredential create(Long principalId, List<IdentifierValue> identifierValues, String password, String userType) {
        return new PasswordCredential(principalId, identifierValues, password, userType);
    }

    /**
     * 设置密码（加密存储）
     */
    private void setPassword(String password) {
        AssertUtil.isTrue(password != null && !password.isBlank(), () -> "密码不能为空");
        this.password = passwordEncoder.get().encode(password);
    }

    /**
     * 变更密码
     */
    public void changePassword(String oldPassword, String newPassword) {
        if (!passwordEncoder.get().matches(oldPassword, this.password)) {
            throw new InvalidCredentialsException("旧密码错误");
        }
        setPassword(newPassword);
    }

    /**
     * 验证密码
     */
    public void verifyPassword(String rawPassword) {
        if(!passwordEncoder.get().matches(rawPassword, this.password)){
            throw new InvalidCredentialsException("密码错误");
        }
    }


    /**
     * 设置标识符列表
     */
    private void setIdentifier(@NotNull List<IdentifierValue> identifierValues, String userType) {
        AssertUtil.isTrue(identifierValues != null && !identifierValues.isEmpty(), () -> "至少需要一个标识符");
        // 检查是否有重复类型的标识符
        long distinctCount = identifierValues.stream()
                .map(IdentifierValue::type)
                .distinct()
                .count();
        if (distinctCount < identifierValues.size()) {
            throw new IllegalArgumentException("标识符类型不能重复");
        }

        identifierValues.forEach(identifierValue -> {
            Identifier identifier = new Identifier(this.getId(), identifierValue.value(), identifierValue.type(), userType);
            this.identifier.add(identifier);
        });
    }
}