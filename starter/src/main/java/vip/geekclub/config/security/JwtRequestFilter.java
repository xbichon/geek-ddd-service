package vip.geekclub.config.security;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.util.AntPathMatcher;
import vip.geekclub.framework.security.AuthSessionManager;
import vip.geekclub.framework.security.UserAuthenticationToken;
import vip.geekclub.framework.exception.JwtParseException;
import vip.geekclub.framework.security.AuthSessionManagerImpl;
import vip.geekclub.security.application.query.PermissionQueryService;

import java.util.Arrays;

import vip.geekclub.framework.utils.HttpUtil;
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
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final AuthSessionManager authSessionManager;
    private String[] PERMIT_PATHS = {};


    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        // 对于免认证路径，完全跳过此过滤器的执行
        return isPermitAllPath(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            httpUtil.getJwtFromRequest(request).ifPresent(tokenValue -> {
                UserAuthenticationToken userAuthenticationToken = authSessionManager.getSession(tokenValue);
                SecurityContextHolder.getContext().setAuthentication(userAuthenticationToken);
            });
            filterChain.doFilter(request, response);
        } catch (JwtParseException e) {
            httpUtil.setResponse(response, ApiResponse.fail(401, e.getMessage()));
        }
    }

    /**
     * 设置免认证路径
     *
     * @param PERMIT_ALL_PATHS 免认证路径数组
     */
    public void setPermitPaths(String[] PERMIT_ALL_PATHS) {
        this.PERMIT_PATHS = PERMIT_ALL_PATHS;
    }

    /**
     * 检查请求路径是否为免认证路径
     *
     * @param requestUri 请求URI
     * @return 如果是免认证路径返回true，否则返回false
     */
    public boolean isPermitAllPath(String requestUri) {
        return Arrays.stream(PERMIT_PATHS)
                .anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }
}