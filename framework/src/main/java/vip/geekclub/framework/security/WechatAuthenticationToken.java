package vip.geekclub.framework.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 微信小程序登录使用的 AuthenticationToken，承载小程序前端传入的 code。
 */
public class WechatAuthenticationToken extends AbstractAuthenticationToken {

    private final String code;

    // 未认证前的构造（仅包含 code）
    public WechatAuthenticationToken(String code) {
        super((Collection<? extends GrantedAuthority>) null);
        this.code = code;
        super.setAuthenticated(false);
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        // 未认证阶段 principal 不可用，本流程返回 LoginToken 后由其承载 JwtToken 作为 principal
        return null;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        // 禁止外部直接设置已认证
        if (isAuthenticated) {
            throw new IllegalArgumentException("Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        }
        super.setAuthenticated(false);
    }
}