package vip.geekclub.config.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A token that represents a user's login session.
 *
 * <p>This token is used to represent a user's login session, which is typically
 * established after a successful login attempt. It extends {@link AbstractAuthenticationToken}
 */
@Getter
public class UserSession implements Authentication {

    private final JwtToken jwtToken;
    private final Object credentials = null;
    private final Object details = null;
    private final boolean authenticated = true;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Set<String> permissions;

    public UserSession(JwtToken principal, Set<String> permissions) {
        List<GrantedAuthority> authorities = permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        authorities.add(new SimpleGrantedAuthority("ROLE_" + principal.userType()));

        this.jwtToken = principal;
        this.permissions = permissions;
        this.authorities = authorities;
        // 移除setAuthenticated(true)调用，因为authenticated字段已经初始化为true
    }

    public UserSession(JwtToken principal) {
        this(principal, Set.of());
    }

    @Override
    public Object getPrincipal() {
        return jwtToken;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("不能设置为已认证状态");
    }

    @Override
    public String getName() {
        return jwtToken.getName();
    }

    public Long getUserId() {
        return jwtToken.userId();
    }

    /**
     * 获取JWT令牌（使用默认过期时间）
     * 
     * @return JWT令牌字符串
     */
    public String getToken() {
        return jwtToken.getToken();
    }
    
    /**
     * 获取JWT令牌（指定过期时间）
     * 
     * @param expirationSeconds 过期时间（秒）
     * @return JWT令牌字符串
     */
    public String getToken(long expirationSeconds) {
        return jwtToken.getToken(expirationSeconds);
    }
}
