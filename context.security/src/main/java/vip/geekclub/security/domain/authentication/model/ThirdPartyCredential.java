package vip.geekclub.security.domain.authentication.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vip.geekclub.framework.domain.model.AggregateRoot;
import vip.geekclub.framework.domain.model.EntitySupport;
import vip.geekclub.security.domain.authentication.value.ThirdPartyType;

@Entity
@Table(name = "security_credential_third_party")
@Getter
@Setter(AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ThirdPartyCredential extends EntitySupport implements AggregateRoot<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long principalId;

    private String authId;
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
    public ThirdPartyCredential(Long principalId, String authId, ThirdPartyType type, String code) {
        setPrincipalId(principalId);
        setAuthId(authId);
        setType(type);
        setCode(code);
    }
}