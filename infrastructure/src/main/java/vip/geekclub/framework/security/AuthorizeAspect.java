package vip.geekclub.framework.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Authorize 注解的 AOP 拦截器
 *
 * <p>拦截带有 @RequireAuth 注解的方法，验证用户角色和权限
 */
@Slf4j
@Aspect
@Component
public class AuthorizeAspect extends AuthorizationChecker {

    public AuthorizeAspect(PermissionStore permissionStore) {
        super(permissionStore);
    }

    @Around("@within(authorize) || @annotation(authorize)")
    public Object around(ProceedingJoinPoint joinPoint, Authorize authorize) throws Throwable {
        check(authorize.userType(), authorize.permissions());
        return joinPoint.proceed();
    }
}