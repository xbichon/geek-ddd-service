package vip.geekclub.contract;

import org.springframework.core.annotation.AliasFor;
import vip.geekclub.framework.security.Authorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 学生授权注解
 *
 * <p>用于方法级别的学生角色和权限验证
 * <p>自动设置用户类型为 STUDENT
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StudentAuthorize {

    /**
     * 需要验证的权限列表
     * <p>为空时只验证学生角色
     * <p>多个权限之间是"并且"关系，必须同时满足
     */
    @AliasFor(annotation = Authorize.class, attribute = "permissions")
    String[] value() default {};
}