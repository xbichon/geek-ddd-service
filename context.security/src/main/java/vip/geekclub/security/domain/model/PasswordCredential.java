package vip.geekclub.security.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.util.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import vip.geekclub.framework.utils.ApplicationUtil;
import vip.geekclub.framework.utils.AssertUtil;
import vip.geekclub.security.domain.value.Identifier;
import vip.geekclub.security.domain.value.IdentifierType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "security_password_credential")
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
    @ElementCollection
    @CollectionTable(
        name = "security_identifier",
        joinColumns = @JoinColumn(name = "password_credential_id")
    )
    private List<Identifier> identifiers = new ArrayList<>();

    /**
     * 加密后的密码
     */
    @Size(max = 200, message = "密码长度不能超过200个字符")
    private String password;

    /**
     * 私有构造函数
     */
    private PasswordCredential(Long principalId, String password, List<Identifier> identifiers) {
        setPrincipalId(principalId);
        setPassword(password);
        setIdentifiers(identifiers);
    }

    /**
     * 创建包含多个标识符的密码凭证
     */
    public static PasswordCredential create(Long principalId, List<Identifier> identifiers, String password) {
        return new PasswordCredential(principalId, password, identifiers);
    }

    /**
     * 添加标识符
     */
    public void addIdentifier(Identifier identifier) {
        if (identifier == null) {
            throw new IllegalArgumentException("标识符不能为空");
        }
        // 检查是否已存在相同类型的标识符
        for (Identifier existing : identifiers) {
            if (existing.type() == identifier.type()) {
                throw new IllegalArgumentException("该类型的标识符已存在");
            }
        }
        identifiers.add(identifier);
    }

    /**
     * 移除标识符
     */
    public void removeIdentifier(Identifier identifier) {
        identifiers.removeIf(i -> i.type() == identifier.type());
    }

    /**
     * 根据类型获取标识符
     */
    public Identifier getIdentifier(IdentifierType type) {
        return identifiers.stream()
            .filter(i -> i.type() == type)
            .findFirst()
            .orElse(null);
    }

    /**
     * 根据类型获取标识符值
     */
    public String getIdentifierValue(IdentifierType type) {
        Identifier identifier = getIdentifier(type);
        return identifier != null ? identifier.value() : null;
    }

    /**
     * 设置密码（加密存储）
     */
    private void setPassword(String password) {
        AssertUtil.isTrue(password != null && !password.isBlank(), () -> "密码不能为空");
        this.password = passwordEncoder.get().encode(password);
    }

    /**
     * 验证密码
     */
    public boolean verifyPassword(String rawPassword) {
        return passwordEncoder.get().matches(rawPassword, this.password);
    }

    /**
     * 变更密码
     */
    public void changePassword(String oldPassword, String newPassword) {
        AssertUtil.isTrue(verifyPassword(oldPassword), () -> "旧密码错误");
        setPassword(newPassword);
    }

    /**
     * 设置标识符列表
     */
    private void setIdentifiers(@NotNull List<Identifier> identifiers) {
        AssertUtil.isTrue(identifiers != null && !identifiers.isEmpty(), () -> "至少需要一个标识符");
        // 检查是否有重复类型的标识符
        long distinctCount = identifiers.stream()
            .map(Identifier::type)
            .distinct()
            .count();
        if (distinctCount < identifiers.size()) {
            throw new IllegalArgumentException("标识符类型不能重复");
        }
        this.identifiers = new ArrayList<>(identifiers);
    }
}