package vip.geekclub.framework.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
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
public class UserAuthentication implements Authentication {

    private final UserPrincipal userPrincipal;
    private final Object credentials = null;
    private final Object details = null;
    @Setter
    private boolean authenticated = true;
    private final Collection<? extends GrantedAuthority> authorities ;

    public UserAuthentication(UserPrincipal principal) {
        this.authorities =List.of(new SimpleGrantedAuthority("ROLE_" + principal.userType()));
        this.userPrincipal = principal;
    }

    @Override
    public UserPrincipal getPrincipal() {
        return this.userPrincipal;
    }


    @Override
    public String getName() {
        return this.userPrincipal.authId();
    }
}
