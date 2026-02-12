package vip.geekclub.contract;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.security.AuthorizationChecker;

/**
 * StudentAuthorize 注解的 AOP 拦截器
 *
 * <p>拦截带有 @StudentAuthorize 注解的方法，验证学生角色和权限
 */
@Slf4j
@Aspect
@Component
public class UserTypeAuthorizeAspect extends AuthorizationChecker {

    @Around("@within(studentAuthorize) || @annotation(studentAuthorize)")
    public Object aroundStudent(ProceedingJoinPoint joinPoint, StudentAuthorize studentAuthorize) throws Throwable {
        check(UserType.STUDENT, studentAuthorize.value());
        return joinPoint.proceed();
    }

    @Around("@within(teacherAuthorize) || @annotation(teacherAuthorize)")
    public Object aroundTeacher(ProceedingJoinPoint joinPoint, TeacherAuthorize teacherAuthorize) throws Throwable {
        check(UserType.TEACHER, teacherAuthorize.value());
        return joinPoint.proceed();
    }
}