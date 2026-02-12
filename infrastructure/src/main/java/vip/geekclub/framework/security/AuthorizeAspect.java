package vip.geekclub.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Set;

/**
 * RequireAuth 注解的 AOP 拦截器
 *
 * <p>拦截带有 @RequireAuth 注解的方法，验证用户角色和权限
 */
@Slf4j @Aspect
@Component
public class AuthorizeAspect {

    @Around("@annotation(requireAuth)")
    public Object around(ProceedingJoinPoint pjp, Authorize requireAuth) throws Throwable {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("用户未登录");
        }

        if (!Objects.equals(requireAuth.userType(), "") && !authentication.getUserPrincipal().userType().equals(requireAuth.userType())) {
            throw new AccessDeniedException("需要 " + requireAuth.userType() + " 角色");
        }

        // 验证权限（如果有指定）
        if (requireAuth.permissions().length > 0) {
            Set<String> userPermissions = authentication.getPermissions();

            for (String permission : requireAuth.permissions()) {
                if (!userPermissions.contains(permission)) {
                    throw new AccessDeniedException("缺少权限: " + permission);
                }
            }
        }

        // 验证通过，执行原方法
        return pjp.proceed();
    }
}