package vip.geekclub.gateway.wechat;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "config.wechat.mini-program")
public class WechatConfig {
    private String appId;
    private String appSecret;
    private String apiBaseUrl;
    private String jscode2sessionUrl;
}