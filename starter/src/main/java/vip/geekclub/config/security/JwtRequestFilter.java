package vip.geekclub.config.security;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import vip.geekclub.framework.security.SessionStore;
import vip.geekclub.framework.security.UserAuthentication;
import vip.geekclub.framework.exception.JwtParseException;

import vip.geekclub.support.HttpUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vip.geekclub.framework.controller.ApiResponse;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 负责从请求中提取JWT令牌，验证其有效性，并设置认证信息
 */
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final HttpUtil httpUtil;
    private final SessionStore authSessionManager;


    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            httpUtil.getJwtFromRequest(request).ifPresent(tokenValue -> {
                UserAuthentication userAuthentication = authSessionManager.load(tokenValue);
                SecurityContextHolder.getContext().setAuthentication(userAuthentication);
            });
            filterChain.doFilter(request, response);
        } catch (JwtParseException e) {
            httpUtil.setResponse(response, ApiResponse.fail(401, e.getMessage()));
        }
    }
}