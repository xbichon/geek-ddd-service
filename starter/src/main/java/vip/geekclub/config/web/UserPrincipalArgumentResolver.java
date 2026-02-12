package vip.geekclub.config.web;

import lombok.NonNull;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import vip.geekclub.framework.security.UserAuthentication;
import vip.geekclub.framework.security.UserPrincipal;

/**
 * UserPrincipal 参数解析器
 * 用于自动将当前登录用户信息注入到控制器方法的 UserPrincipal 参数中
 */
@Component
public class UserPrincipalArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserPrincipal.class);
    }

    @Override
    public Object resolveArgument(@NonNull MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  @NonNull NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UserAuthentication) {
            return ((UserAuthentication) authentication).getPrincipal();
        }

        return null;
    }
}