package vip.geekclub.security.auth.adapter.wechat;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class WechatService {
    
    private final WechatConfig config;
    private final RestTemplate restTemplate;
    
    public WechatSessionResponse getSessionInfo(String code) {
        String url = UriComponentsBuilder
                .fromUriString(config.getApiBaseUrl() + config.getJscode2sessionUrl())
                .queryParam("appid", config.getAppId())
                .queryParam("secret", config.getAppSecret())
                .queryParam("js_code", code)
                .queryParam("grant_type", "authorization_code")
                .toUriString();
        
        log.info("调用微信API获取session信息: {}", url.replaceAll("secret=[^&]*", "secret=***"));
        
        try {
            WechatSessionResponse response = restTemplate.getForObject(url, WechatSessionResponse.class);
            if (response != null && response.isSuccess()) {
                log.error("微信API调用失败: errcode={}, errmsg={}", response.errCode(), response.errMsg());
            }
            return response;
        } catch (Exception e) {
            log.error("调用微信API异常", e);
            throw new RuntimeException("调用微信API失败", e);
        }
    }
    
    /**
     * 获取微信小程序用户的 unionid
     * @param code 微信授权码
     * @return unionid，如果获取失败则抛出异常
     */
    public String getUnionId(String code) {
        WechatSessionResponse session = getSessionInfo(code);
        if (session == null || session.isSuccess()) {
            throw new RuntimeException("微信授权失败: " + (session != null ? session.errMsg() : "未知错误"));
        }
        
        String unionId = session.unionId();
        if (unionId == null || unionId.isBlank()) {
            // 如果没有 unionId，可能是因为用户未关注公众号或开发者账号配置问题
            // 这种情况下可以考虑降级使用 openId，或者要求用户先关注公众号
            log.warn("未获取到 unionId，可能需要用户关注公众号或检查开发者账号配置");
            throw new RuntimeException("获取微信UnionID失败，请确保已关注相关公众号");
        }
        
        return unionId;
    }
}