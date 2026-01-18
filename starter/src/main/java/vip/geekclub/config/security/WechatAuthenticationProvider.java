package vip.geekclub.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import vip.geekclub.common.security.JwtAuthentication;
import vip.geekclub.common.security.JwtPrincipal;
import vip.geekclub.common.security.WechatAuthenticationToken;
import vip.geekclub.security.auth.domain.AuthenticationType;
import vip.geekclub.security.auth.application.query.AuthenticationQueryService;
import vip.geekclub.security.auth.application.query.dto.CredentialResult;

import java.util.Objects;

/**
 * 微信小程序登录 Provider：
 * 1) 接收 unionId 作为用户唯一标识
 * 2) 依据 unionId 查询凭证（类型 WECHAT）
 * 3) 成功则返回 UserSession（包含 JwtToken）
 */
@Service
@RequiredArgsConstructor
public class WechatAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationQueryService authenticationQueryService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        WechatAuthenticationToken token = (WechatAuthenticationToken) authentication;
        String unionId = Objects.toString(token.getCredentials(), "");

        if (unionId.isBlank()) {
            throw new BadCredentialsException("微信UnionID不能为空");
        }

        // 查询 WECHAT 凭证（使用 unionId）
        CredentialResult credential = authenticationQueryService
                .getAuthenticationByIdentifier(unionId, AuthenticationType.WECHAT)
                .orElseThrow(() -> new BadCredentialsException("用户未注册，请先绑定微信账号"));

        // 构建并返回 UserSession（内含 JwtToken）
        JwtPrincipal jwtPrincipal = new JwtPrincipal(credential.userId(), credential.accountType().toString());
        return new JwtAuthentication(jwtPrincipal);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return WechatAuthenticationToken.class.equals(authentication);
    }
}