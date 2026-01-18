package vip.geekclub.config.security;

import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.utils.HttpUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    /**
     * 免认证路径
     */
    public static final String[] PERMIT_PATHS = {"/auth/**", "/test/**"};
    private static final String SECURITY_PATH = "/**";
    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    private static final String[] ALLOWED_HEADERS = {"*"};
    private static final long CORS_MAX_AGE = 3600L;

    private final JwtRequestFilter jwtRequestFilter;
    private final HttpUtil httpUtil;

    public SecurityConfig(JwtRequestFilter jwtRequestFilter, HttpUtil httpUtil) {
        jwtRequestFilter.setPermitPaths(PERMIT_PATHS);
        this.jwtRequestFilter = jwtRequestFilter;
        this.httpUtil = httpUtil;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置认证管理器
     */
    @Bean
    public AuthenticationManager authenticationManager(
            WechatAuthenticationProvider wechatAuthenticationProvider,
            UserNameAuthenticationProvider userNameAuthenticationProvider) {

        // 直接创建ProviderManager，不使用默认AuthenticationManager作为父级
        // 这样避免了可能的循环引用导致堆栈溢出
        return new ProviderManager(List.of(
                wechatAuthenticationProvider,
                userNameAuthenticationProvider));
    }

    /**
     * 配置安全过滤器链
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) {
        http.authenticationManager(authenticationManager);
        configureUrl(http);
        configureCross(http);
        configureFilter(http);
        configureExceptionHandling(http);
        disableUnnecessaryFilters(http);
        return http.build();
    }

    /**
     * 禁用不必要的过滤器，优化性能
     */
    private void disableUnnecessaryFilters(HttpSecurity http) {
        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(AbstractHttpConfigurer::disable);
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.rememberMe(AbstractHttpConfigurer::disable);
        http.anonymous(AbstractHttpConfigurer::disable); // 禁用匿名认证
        http.logout(AbstractHttpConfigurer::disable); // 禁用注销功能
        http.requestCache(AbstractHttpConfigurer::disable); // 禁用请求缓存
    }

    /**
     * 配置URL拦截规则
     */
    private void configureUrl(HttpSecurity http) {
        http.securityMatcher(SECURITY_PATH);
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers(PERMIT_PATHS).permitAll()
                .anyRequest().authenticated());
    }

    /**
     * 配置CORS
     */
    public void configureCross(HttpSecurity http) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOriginPatterns(List.of("*")); // 允许所有域名（生产环境应指定具体域名）
        corsConfiguration.setAllowedMethods(List.of(ALLOWED_METHODS));
        corsConfiguration.setAllowedHeaders(List.of(ALLOWED_HEADERS));
        corsConfiguration.setAllowCredentials(true); // 允许携带 Cookie
        corsConfiguration.setMaxAge(CORS_MAX_AGE); // 预检请求缓存时间（秒）

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(SECURITY_PATH, corsConfiguration); // 全局生效

        http.cors(config -> config.configurationSource(source));
    }

    /**
     * 配置过滤器
     */
    private void configureFilter(HttpSecurity http) {
        // 添加JWT请求过滤器，在用户名密码认证过滤器之前执行
        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 配置异常处理
     */
    private void configureExceptionHandling(HttpSecurity http) {
        http.exceptionHandling(config -> config
                // 请求未授权接口处理
                .accessDeniedHandler((request, response, exception) -> httpUtil.setResponse(response,
                        ApiResponse.fail(403, "用户无权限")))

                // 请求未认证接口处理
                .authenticationEntryPoint((request, response, exception) -> httpUtil.setResponse(response,
                        ApiResponse.fail(401, exception.getMessage()))));
    }

}