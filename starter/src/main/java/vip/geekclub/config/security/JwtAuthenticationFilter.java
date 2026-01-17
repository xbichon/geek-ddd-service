package vip.geekclub.config.security;

import lombok.NonNull;
import org.springframework.util.AntPathMatcher;
import vip.geekclub.common.exception.JwtParseException;
import vip.geekclub.security.permission.query.PermissionQueryService;

import java.util.Arrays;
import java.util.Set;

import vip.geekclub.common.utils.HttpUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vip.geekclub.common.controller.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT认证过滤器
 * 负责从请求中提取JWT令牌，验证其有效性，并设置认证信息
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final HttpUtil httpUtil;
    private final PermissionQueryService permissionQueryService;
    private final AntPathMatcher pathMatcher =new AntPathMatcher();
    private String[] PERMIT_ALL_PATHS={} ;

    public JwtAuthenticationFilter(HttpUtil httpUtil, PermissionQueryService permissionQueryService) {
        this.httpUtil = httpUtil;
        this.permissionQueryService = permissionQueryService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // 使用统一配置检查是否为免认证路径
        if (!isPermitAllPath(request.getRequestURI())) {
            try {
                httpUtil.getJwtFromRequest(request)
                        .ifPresent(this::setAuthentication);
            } catch (JwtParseException e) {
                httpUtil.setResponse(response, ApiResponse.fail(401, e.getMessage()));
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String tokenValue) {
        JwtToken jwtToken = JwtToken.buildByToken(tokenValue);
        Set<String> permissions = permissionQueryService.getPermissionByUserId(jwtToken.userId());

        UserSession userSession = new UserSession(jwtToken, permissions);
        SecurityContextHolder.getContext().setAuthentication(userSession);
    }

    /**
     * 设置免认证路径
     *
     * @param PERMIT_ALL_PATHS 免认证路径数组
     */
    public void setPermitAllPaths(String[] PERMIT_ALL_PATHS) {
        this.PERMIT_ALL_PATHS = PERMIT_ALL_PATHS;
    }

    /**
     * 检查请求路径是否为免认证路径
     *
     * @param requestUri 请求URI
     * @return 如果是免认证路径返回true，否则返回false
     */
    public boolean isPermitAllPath(String requestUri) {
        return Arrays.stream(PERMIT_ALL_PATHS)
                .anyMatch(pattern -> pathMatcher.match(pattern, requestUri));
    }
}