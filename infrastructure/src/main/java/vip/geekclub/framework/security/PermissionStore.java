package vip.geekclub.framework.security;

import java.util.Set;

public interface PermissionStore {

    Set<String> getPermissions(UserPrincipal userPrincipal);
}
