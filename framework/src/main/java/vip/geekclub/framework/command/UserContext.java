package vip.geekclub.framework.command;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import vip.geekclub.framework.security.UserAuthenticationToken;
import vip.geekclub.framework.security.JwtToken;
import vip.geekclub.framework.security.UserPrincipal;

/**
 * 用户上下文
 * 使用 ThreadLocal 存储当前请求的用户信息
 */
public class UserContext {

    private static final ThreadLocal<UserPrincipal> CURRENT_USER = new ThreadLocal<>();

    /**
     * 从 Spring Security Context 中提取当前用户信息并设置到 ThreadLocal
     */
    public static void extractFromSecurityContext() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication instanceof UserAuthenticationToken userAuthenticationToken) {
            setCurrentUser(userAuthenticationToken.getPrincipal());
        }
    }

    /**
     * 清除当前用户信息
     */
    public static void clear() {
        CURRENT_USER.remove();
    }

    /**
     * 设置当前用户
     */
    public static void setCurrentUser(UserPrincipal principal) {
        CURRENT_USER.set(principal);
    }

    /**
     * 获取当前用户
     */
    public static UserPrincipal getCurrentUser() {
        return CURRENT_USER.get();
    }

    /**
     * 检查是否有当前用户
     */
    public static boolean hasCurrentUser() {
        return CURRENT_USER.get() != null;
    }
}