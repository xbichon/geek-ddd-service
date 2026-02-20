package vip.geekclub.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import vip.geekclub.contract.UserType;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.StatelessBaseConfigurer;
import vip.geekclub.framework.utils.HttpUtil;

import java.util.List;

/**
 * Spring Security 安全配置类
 * 采用双过滤器链设计：
 * 1. 白名单链（@Order(1)）：处理免认证路径，不经过 JWT 过滤器
 * 2. 安全链（@Order(2)）：处理需认证路径，经过 JWT 过滤器进行身份验证
 */
@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
//@EnableMethodSecurity
public class SecurityConfig {

    /**
     * 免认证路径（白名单）
     * 匹配这些路径的请求不会经过 JWT 过滤器
     */
    private static final String[] PERMIT_PATHS = {"/security/auth/**", "/test/**"};

    /**
     * 安全路径匹配规则
     * 匹配所有请求，但白名单路径会优先被 whiteListChain 处理
     */
    private static final String SECURITY_PATH = "/**";

    /**
     * CORS 允许的 HTTP 方法
     */
    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};

    /**
     * CORS 允许的请求头
     */
    private static final String[] ALLOWED_HEADERS = {"*"};

    /**
     * CORS 预检请求缓存时间（秒）
     */
    private static final long CORS_MAX_AGE = 3600L;

    private final HttpUtil httpUtil;

    private final JwtRequestFilter jwtRequestFilter;

    /**
     * 禁用 JWT 过滤器的自动注册
     * 防止 Spring Boot 将其注册为全局 Servlet Filter
     * 我们只在安全链中手动添加该过滤器
     */
    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtRequestFilterRegistration(JwtRequestFilter filter) {
        FilterRegistrationBean<JwtRequestFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    /**
     * 密码编码器
     * 用于加密和验证用户密码
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 白名单过滤器链（优先级：1）
     * 处理免认证路径，特点：
     * - 匹配 PERMIT_PATHS 定义的路径
     * - 不添加 JWT 过滤器
     * - 所有请求直接放行（permitAll）
     * - 保留基本的 CORS 支持
     */
    @Bean
    @Order(1)
    public SecurityFilterChain whiteListChain(HttpSecurity http) {
        http.securityMatcher(PERMIT_PATHS)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .apply(new StatelessBaseConfigurer());

        return http.build();
    }

    /**
     * 安全过滤器链（优先级：2）
     * 处理需认证路径，特点：
     * - 匹配所有未被白名单链处理的请求
     * - 添加 JWT 过滤器进行身份验证
     * - 按角色配置访问权限
     * - 配置认证/授权异常处理
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securedChain(HttpSecurity http) {
        http.securityMatcher(SECURITY_PATH)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/student/**").hasRole(UserType.STUDENT)
                        .requestMatchers("/teacher/**").hasRole(UserType.TEACHER)
                        .anyRequest().authenticated())
                .exceptionHandling(config -> config
                        .accessDeniedHandler(
                                (request, response, exception) ->
                                        httpUtil.setResponse(response, ApiResponse.fail(403, "用户无权限"))
                        )
                        .authenticationEntryPoint(
                                (request, response, exception) ->
                                        httpUtil.setResponse(response, ApiResponse.fail(401, exception.getMessage()))
                        )
                )
                .apply(new StatelessBaseConfigurer());

        return http.build();
    }

    /**
     * CORS 跨域配置
     * 允许所有来源、方法和请求头
     * 注意：生产环境应指定具体允许域名
     */
    private void configureCors(HttpSecurity http) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of(ALLOWED_METHODS));
        corsConfiguration.setAllowedHeaders(List.of(ALLOWED_HEADERS));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(CORS_MAX_AGE);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(SECURITY_PATH, corsConfiguration);

        http.cors(config -> config.configurationSource(source));
    }
}