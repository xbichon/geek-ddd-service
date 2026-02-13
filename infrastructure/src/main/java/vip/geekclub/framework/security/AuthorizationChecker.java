package vip.geekclub.framework.security;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import vip.geekclub.framework.exception.BusinessException;

import java.util.Objects;
import java.util.Set;

/**
 * 授权验证器
 *
 * <p>提供统一的用户类型和权限验证逻辑，可被切面或其他组件复用
 */
public class AuthorizationChecker {

    /**
     * 执行授权验证
     *
     * @param userType    要求的用户类型，为空字符串时跳过验证
     * @param permissions 要求的权限列表，为空时跳过验证
     * @throws AccessDeniedException 验证失败时抛出
     */
    public void check(String userType, String[] permissions) {
        UserAuthentication authentication = getCurrentAuthentication();
        checkUserType(authentication, userType);
        checkPermissions(authentication, permissions);
    }

    /**
     * 验证用户类型
     */
    protected void checkUserType(UserAuthentication authentication, String userType) {
        if (Objects.equals(userType, "")) {
            return;
        }

        String actualUserType = authentication.getUserPrincipal().userType();
        if (!actualUserType.equals(userType)) {
            throw new AccessDeniedException("需要 " + userType + " 用户类型");
        }
    }

    /**
     * 验证权限列表
     */
    protected void checkPermissions(UserAuthentication authentication, String[] permissions) {
        if (permissions == null || permissions.length == 0) {
            return;
        }

        Set<String> userPermissions = authentication.getPermissions();

        for (String permission : permissions) {
            if (!userPermissions.contains(permission)) {
                throw new BusinessException(403,"缺少权限: " + permission);
            }
        }
    }

    /**
     * 获取当前认证信息
     */
    protected UserAuthentication getCurrentAuthentication() {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new BusinessException(401,"用户未登录");
        }
        return authentication;
    }
}