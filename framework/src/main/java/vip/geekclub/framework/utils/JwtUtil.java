package vip.geekclub.framework.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.exception.JwtParseException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * JWT工具类
 * 用于生成、验证和解析JWT令牌
 *
 * @author leo
 * @since 1.0
 */
@Slf4j
@Component
@ConfigurationProperties(prefix = "config.jwt")
@Data
public class JwtUtil {

    // ================================ 配置属性 ================================

    private final static String secretKeyPrefix = "GeKV@";

    /**
     * JWT签名密钥
     */
    private String secretKey = "Geek2024SecurityDianComChina12987T23001Y-Q8Y05OOD";

    /**
     * 过期时间（天）
     */
    private Long expirationDays = 7L;

    /**
     * 发行人
     */
    private String issuer = "geekclub.vip";

    /**
     * 算法
     */
    private String algorithm = "HS256";

    // ================================ 核心方法 ================================

    /**
     * 生成JWT令牌（指定过期时间）
     *
     * @param subject           主题（通常是用户ID）
     * @param claims            声明信息
     * @param expirationSeconds 过期时间（秒）
     * @return JWT令牌字符串
     */
    public String generateToken(String subject, Map<String, Object> claims, long expirationSeconds) {
        try {
            // 计算过期时间
            Date expirationDate = Date.from(Instant.now().plusSeconds(expirationSeconds));
            SecretKey key = generateHmacShaKey();

            return Jwts.builder()
                    .header()
                    .add("typ", "JWT")
                    .add("alg", "HS256")
                    .and()
                    .id(UUID.randomUUID().toString())
                    .subject(subject)
                    .expiration(expirationDate)
                    .issuedAt(Date.from(Instant.now()))
                    .issuer(issuer)
                    .claims(claims)
                    .signWith(key)
                    .compact();
        } catch (Exception e) {
            log.error("生成JWT令牌失败: subject={}, error={}", subject, e.getMessage(), e);
            throw new RuntimeException("生成JWT令牌失败", e);
        }
    }


    /**
     * 生成JWT令牌（将对象序列化为JSON存入data字段）
     *
     * @param data              数据对象
     * @param expirationSeconds 过期时间（秒）
     * @param <T>               数据类型
     * @return JWT令牌字符串
     */
    public <T> String generateToken(String subject, T data, long expirationSeconds) {
        try {
            String jsonData = JsonUtils.getObjectMapper().writeValueAsString(data);
            Map<String, Object> claims = Map.of("data", jsonData);
            return generateToken(subject, claims, expirationSeconds);
        } catch (Exception e) {
            log.error("生成JWT令牌失败: data={}, error={}", data, e.getMessage(), e);
            throw new RuntimeException("生成JWT令牌失败", e);
        }
    }

    /**
     * 解析token
     */
    public Claims parseToken(String token) {

        if (token == null || token.trim().isEmpty()) {
            throw new JwtParseException("令牌不能为空");
        }
        try {
            return getJwtParser().parseSignedClaims(token).getPayload();
        } catch (ExpiredJwtException e) {
            throw new JwtParseException("令牌已过期", e);
        } catch (UnsupportedJwtException e) {
            throw new JwtParseException("不支持的令牌格式", e);
        } catch (MalformedJwtException e) {
            throw new JwtParseException("令牌格式错误", e);
        } catch (SecurityException e) {
            throw new JwtParseException("令牌签名验证失败", e);
        } catch (IllegalArgumentException e) {
            throw new JwtParseException("令牌参数错误", e);
        } catch (Exception e) {
            throw new JwtParseException("令牌解析失败", e);
        }
    }

    /**
     * 解析token（从data字段反序列化为指定类型对象）
     *
     * @param token JWT令牌字符串
     * @param type  目标类型
     * @param <T>   数据类型
     * @return 反序列化后的对象
     */
    public <T> T parseToken(String token, Class<T> type) {
        Claims claims = parseToken(token);
        Object data = claims.get("data");
        if (data == null) {
            throw new JwtParseException("令牌中缺少data字段");
        }
        try {
            return JsonUtils.getObjectMapper().readValue(data.toString(), type);
        } catch (Exception e) {
            log.error("解析token数据失败: data={}, type={}, error={}", data, type.getName(), e.getMessage(), e);
            throw new JwtParseException("解析token数据失败", e);
        }
    }

    // ================================ 私有方法 ================================

    /**
     * 获取签名密钥
     */
    private SecretKey generateHmacShaKey() {
        return Keys.hmacShaKeyFor((secretKeyPrefix + "-" + secretKey).getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 获取JWT解析器
     */
    private JwtParser getJwtParser() {
        return Jwts.parser().verifyWith(generateHmacShaKey()).build();
    }
}