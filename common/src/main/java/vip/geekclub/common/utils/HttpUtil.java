package vip.geekclub.common.utils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Optional;

@Component
@AllArgsConstructor
public class HttpUtil {

    private final String AUTHORIZATION_KEY = "authorization";
    private final String JWT_TYPE = "Bearer ";
    private ObjectMapper objectMapper;

    /**
     * 统一处理返回结果
     */
    public void setResponse(HttpServletResponse response, Object content) throws IOException {
        String json = objectMapper.writeValueAsString(content);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().println(json);
    }

    /**
     * 设置JWT令牌到HTTP响应头
     *
     * @param response HTTP响应对象
     * @param encode   编码后的JWT令牌字符串
     */
    public void setJwtToResponse(HttpServletResponse response, String encode) {
        response.setHeader(AUTHORIZATION_KEY, JWT_TYPE + encode);
    }

    /**
     * 从HTTP请求头中获取JWT令牌
     *
     * @param request HTTP请求对象
     * @return 包含JWT令牌的Optional对象，如果请求头中不存在JWT令牌，则返回空的Optional对象
     */
    public Optional<String> getJwtFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_KEY);

        if (authorization == null || authorization.isBlank() || !authorization.startsWith(JWT_TYPE)) {
            return Optional.empty();
        } else {
            authorization = authorization.substring(JWT_TYPE.length());
            return Optional.of(authorization);
        }
    }
}
