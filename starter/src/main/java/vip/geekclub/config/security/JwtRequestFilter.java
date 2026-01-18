package vip.geekclub.config.security;

import lombok.NonNull;
import org.springframework.util.AntPathMatcher;
import vip.geekclub.framework.exception.JwtParseException;
import vip.geekclub.framework.security.JwtAuthentication;
import vip.geekclub.framework.security.JwtPrincipal;
import vip.geekclub.security.permission.application.query.PermissionQueryService;

import java.util.Arrays;
import java.util.Set;

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
public class JwtRequestFilter extends OncePerRequestFilter {

    private final HttpUtil httpUtil;
    private final PermissionQueryService permissionQueryService;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private String[] PERMIT_PATHS = {};

    public JwtRequestFilter(HttpUtil httpUtil, PermissionQueryService permissionQueryService) {
        this.httpUtil = httpUtil;
        this.permissionQueryService = permissionQueryService;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        // 对于免认证路径，完全跳过此过滤器的执行
        return isPermitAllPath(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            httpUtil.getJwtFromRequest(request).ifPresent(this::setAuthentication);
            filterChain.doFilter(request, response);
        } catch (JwtParseException e) {
            httpUtil.setResponse(response, ApiResponse.fail(401, e.getMessage()));
        }
    }

    private void setAuthentication(String tokenValue) {
        JwtPrincipal jwtPrincipal = JwtPrincipal.buildByToken(tokenValue);
        Set<String> permissions = permissionQueryService.getPermissionByUserId(jwtPrincipal.userId());

        JwtAuthentication jwtAuthentication = new JwtAuthentication(jwtPrincipal, permissions);
        SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
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