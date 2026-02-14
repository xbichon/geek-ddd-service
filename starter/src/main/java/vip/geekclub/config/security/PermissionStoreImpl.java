package vip.geekclub.config.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.security.PermissionStore;
import vip.geekclub.framework.security.UserAuthentication;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.security.application.query.PermissionQueryService;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PermissionStoreImpl implements PermissionStore {

    private final PermissionQueryService permissionQueryService;

    @Override
    public Set<String> getPermissions(@NonNull UserPrincipal principal) {
        return permissionQueryService.getPermissionByAuthId(principal.authId());
    }
}
