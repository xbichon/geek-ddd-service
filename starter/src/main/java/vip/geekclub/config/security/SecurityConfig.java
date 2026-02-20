package vip.geekclub.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import vip.geekclub.contract.UserType;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.utils.HttpUtil;

import java.util.List;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
@EnableMethodSecurity()
public class SecurityConfig {

    /**
     * 免认证路径
     */
    public static final String[] PERMIT_PATHS = {"/security/auth/**", "/test/**"};
    private static final String SECURITY_PATH = "/**";
    private static final String[] ALLOWED_METHODS = {"GET", "POST", "PUT", "DELETE", "OPTIONS"};
    private static final String[] ALLOWED_HEADERS = {"*"};
    private static final long CORS_MAX_AGE = 3600L;

    private final HttpUtil httpUtil;
    private final JwtRequestFilter jwtRequestFilter;

    @Bean
    public FilterRegistrationBean<JwtRequestFilter> jwtRequestFilterRegistration(JwtRequestFilter filter) {
        FilterRegistrationBean<JwtRequestFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 白名单链：免认证路径，无JWT过滤器
     */
    @Bean
    @Order(1)
    public SecurityFilterChain whiteListChain(HttpSecurity http) {
        http.securityMatcher(PERMIT_PATHS)
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        configureCommon(http);
        return http.build();
    }

    /**
     * 安全链：需认证路径，有JWT过滤器
     */
    @Bean
    @Order(2)
    public SecurityFilterChain securedChain(HttpSecurity http) {
        http.securityMatcher(SECURITY_PATH)
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/student/**").hasRole(UserType.STUDENT)
                        .requestMatchers("/teacher/**").hasRole(UserType.TEACHER)
                        .anyRequest().authenticated());
        configureCommon(http);
        configureExceptionHandling(http);
        return http.build();
    }

    /**
     * 公共配置：禁用不必要的过滤器、CORS、Session策略
     */
    private void configureCommon(HttpSecurity http) {
        http.csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .rememberMe(AbstractHttpConfigurer::disable)
                .anonymous(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .requestCache(AbstractHttpConfigurer::disable)
                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 跨域配置
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


    /**
     * 配置异常处理
     */
    private void configureExceptionHandling(HttpSecurity http) {
        http.exceptionHandling(config -> config
                .accessDeniedHandler((request, response, exception) -> httpUtil.setResponse(response,
                        ApiResponse.fail(403, "用户无权限")))
                .authenticationEntryPoint((request, response, exception) -> httpUtil.setResponse(response,
                        ApiResponse.fail(401, exception.getMessage()))));
    }
}