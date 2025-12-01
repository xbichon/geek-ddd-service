package vip.geekclub.config;

import vip.geekclub.common.controller.ApiResponse;
import vip.geekclub.common.utils.HttpUtil;
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
import vip.geekclub.config.security.*;

import java.util.List;

@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    public static final String[] PERMIT_ALL_PATHS = {"/auth/**", "/test/**"};
    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    private static final String[] ALLOWED_HEADERS = {"*"};
    private static final long CORS_MAX_AGE = 3600L;
    private static final String SECURITY_PATH = "/**";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final HttpUtil httpUtil;


    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, HttpUtil httpUtil) {
        jwtAuthenticationFilter.setPermitAllPaths(PERMIT_ALL_PATHS);
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.httpUtil = httpUtil;
    }

    // 常量定义


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager)
            throws Exception {
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
    private void disableUnnecessaryFilters(HttpSecurity http) throws Exception {

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
    private void configureUrl(HttpSecurity http) throws Exception {
        http.securityMatcher(SECURITY_PATH);
        http.authorizeHttpRequests(authorize -> authorize.requestMatchers(PERMIT_ALL_PATHS).permitAll()
                .anyRequest().authenticated());
    }

    /**
     * 配置CORS
     */
    public void configureCross(HttpSecurity http) throws Exception {
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

    private void configureFilter(HttpSecurity http) {
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

    private void configureExceptionHandling(HttpSecurity http) throws Exception {
        http.exceptionHandling(config -> config
                // 请求未授权接口处理
                .accessDeniedHandler((request, response, exception) -> httpUtil.setResponse(response,
                        ApiResponse.fail(403, "用户无权限")))

                // 请求未认证接口处理
                .authenticationEntryPoint((request, response, exception) -> httpUtil.setResponse(response,
                        ApiResponse.fail(401, exception.getMessage()))));
    }

}