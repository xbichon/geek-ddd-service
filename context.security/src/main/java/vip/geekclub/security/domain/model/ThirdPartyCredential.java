package vip.geekclub.security.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.security.domain.value.ThirdPartyType;

@Entity
@Table(name = "security_credential_third_party")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThirdPartyCredential extends Credential {
    /**
     * 第三方服务提供商，例如微信、支付宝等
     */
    @Enumerated(jakarta.persistence.EnumType.STRING)
    private ThirdPartyType type;

    /**
     * 第三方标识符，例如微信 openId
     */
    @Size(min = 1, max = 200, message = "标识符长度必须在1-200个字符之间")
    private String code;

    /**
     * 构造函数
     */
    public ThirdPartyCredential(Long principalId, ThirdPartyType type, String code) {
        setPrincipalId(principalId);
        setType(type);
        setCode(code);
    }
}