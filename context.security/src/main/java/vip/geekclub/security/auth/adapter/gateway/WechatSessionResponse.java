package vip.geekclub.security.auth.adapter.gateway;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WechatSessionResponse(
    @JsonProperty("openid")
    String openId,
    
    @JsonProperty("session_key")
    String sessionKey,
    
    @JsonProperty("unionid")
    String unionId,
    
    @JsonProperty("errcode")
    Integer errCode,
    
    @JsonProperty("errmsg")
    String errMsg
) {
    public boolean isSuccess() {
        return errCode != null && errCode != 0;
    }
}