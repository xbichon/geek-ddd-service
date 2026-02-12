package vip.geekclub.framework.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 认证授权注解
 *
 * <p>用于方法级别的角色和权限验证
 *
 * <p>示例：
 * <pre>
 * // 只验证角色
 * &#064;RequireAuth(role  = "TEACHER")
 *
 * // 验证角色和单个权限
 * &#064;RequireAuth(role  = "TEACHER", permissions = "dept:create")
 *
 * // 验证角色和多个权限（并且关系）
 * &#064;RequireAuth(role  = "TEACHER", permissions = {"dept:create", "dept:delete"})
 * </pre>
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authorize {

    /**
     * 角色名称（如 TEACHER、STUDENT、ADMIN）
     */
    String userType() default "";

    /**
     * 额外需要验证的权限列表
     * <p>为空时只验证角色
     * <p>多个权限之间是"并且"关系，必须同时满足
     */
    String[] permissions() default {};
}