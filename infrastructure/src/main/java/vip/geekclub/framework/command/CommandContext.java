package vip.geekclub.framework.command;


import vip.geekclub.framework.security.UserPrincipal;

/**
 * 用户上下文
 * 使用 ThreadLocal 存储当前请求的用户信息
 */
public class CommandContext {

    private static final ThreadLocal<UserPrincipal> CURRENT_USER = new ThreadLocal<>();

    /**
     * 设置当前用户
     */
    public static void setCurrentUser(UserPrincipal principal) {
        CURRENT_USER.set(principal);
    }

    /**
     * 获取当前用户
     */
    public static UserPrincipal getCurrentPrincipal() {

        if (!hasCurrentUser()) {
            throw new IllegalStateException("获取当前用户失败，用户未登录");
        }
        return CURRENT_USER.get();
    }

    /**
     * 检查是否有当前用户
     */
    public static boolean hasCurrentUser() {
        return CURRENT_USER.get() != null;
    }

    /**
     * 清除当前用户信息
     */
    public static void clear() {
        CURRENT_USER.remove();
    }
}