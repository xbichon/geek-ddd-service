package vip.geekclub.contract;

import org.springframework.core.annotation.AliasFor;
import vip.geekclub.framework.security.Authorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 教师授权注解
 *
 * <p>用于方法级别的教师角色和权限验证
 * <p>自动设置用户类型为 TEACHER
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface TeacherAuthorize {

    /**
     * 需要验证的权限列表
     * <p>为空时只验证教师角色
     * <p>多个权限之间是"并且"关系，必须同时满足
     */
    String[] value() default {};
}