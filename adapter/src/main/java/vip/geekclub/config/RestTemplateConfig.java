package vip.geekclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        
        // 创建支持text/plain的JSON转换器
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        jsonConverter.setSupportedMediaTypes(Arrays.asList(
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN,  // 支持text/plain格式的JSON
            new MediaType("application", "*+json")
        ));
        
        // 获取现有的消息转换器列表
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        // 在列表开头添加我们的JSON转换器，确保优先使用
        messageConverters.addFirst(jsonConverter);
        
        return restTemplate;
    }
}