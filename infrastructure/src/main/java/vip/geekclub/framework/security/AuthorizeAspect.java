package vip.geekclub.framework.security;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.jspecify.annotations.NonNull;
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
@Slf4j
@Aspect
@Component
public class AuthorizeAspect {

    @Around("@annotation(authorize)")
    public Object around(ProceedingJoinPoint pjp, Authorize authorize) throws Throwable {
        UserAuthentication authentication = getUserAuthentication(authorize);

        String userType = authorize.userType();
        if (!Objects.equals(userType, "") && !authentication.getUserPrincipal().userType().equals(userType)) {
            throw new AccessDeniedException("需要 " + authorize.userType() + " 角色");
        }

        if (authorize.permissions().length > 0) {
            Set<String> userPermissions = authentication.getPermissions();

            for (String permission : authorize.permissions()) {
                if (!userPermissions.contains(permission)) {
                    throw new AccessDeniedException("缺少权限: " + permission);
                }
            }
        }

        // 验证通过，执行原方法
        return pjp.proceed();
    }

    private static @NonNull UserAuthentication getUserAuthentication(Authorize authorize) {
        UserAuthentication authentication = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("用户未登录");
        }

        return authentication;
    }
}