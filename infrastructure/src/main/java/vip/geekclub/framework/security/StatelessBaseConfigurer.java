package vip.geekclub.framework.security;

import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.DefaultSecurityFilterChain;

/**
   * 基础无状态配置器（复用单元）
   * 封装所有共享的禁用配置
   */
  public class StatelessBaseConfigurer
      extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

      @Override
      public void configure(HttpSecurity http) {
          http
              .csrf(AbstractHttpConfigurer::disable)
              .httpBasic(AbstractHttpConfigurer::disable)
              .formLogin(AbstractHttpConfigurer::disable)
              .rememberMe(AbstractHttpConfigurer::disable)
              .anonymous(AbstractHttpConfigurer::disable)
              .logout(AbstractHttpConfigurer::disable)
              .requestCache(AbstractHttpConfigurer::disable)
              .sessionManagement(config ->
                  config.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
      }
  }