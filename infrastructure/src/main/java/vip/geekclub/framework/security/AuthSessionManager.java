package vip.geekclub.framework.security;

import vip.geekclub.framework.utils.AssertUtil;

import java.util.Map;
import java.util.Set;

public interface AuthSessionManager {
    String createSession(UserAuthenticationToken authentication);

    UserAuthenticationToken getSession(String token);
}
