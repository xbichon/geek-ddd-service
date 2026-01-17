package vip.geekclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * RestTemplate配置
 *
 * @author : leo
 * <p>
 * Create Time 2025-01-12
 * Copyright 2025 www.geekclub.vip Inc. All rights reserved.
 */
@Configuration
public class RestTemplateConfig {

    /**
     * 创建RestTemplate Bean
     * 配置适合微信API调用的超时时间
     *
     * @return RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        // 设置连接超时时间为10秒（单位：毫秒）
        factory.setConnectTimeout(10 * 1000);
        // 设置读取超时时间为30秒（微信API可能响应较慢）
        factory.setReadTimeout(30 * 1000);

        return new RestTemplate(factory);
    }
}