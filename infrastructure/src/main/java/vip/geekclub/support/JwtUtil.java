package vip.geekclub.support;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
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

    // ================================ 常量定义 ================================

    private static final String SECRET_KEY_PREFIX = "GeKV@";
    private static final String JWT_HEADER_TYPE = "JWT";
    private static final String JWT_HEADER_ALGORITHM = "HS256";
    private static final String DATA_FIELD = "data";

    // ================================ 配置属性 ================================

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

    // ================================ 缓存对象 ================================

    private SecretKey cachedKey;
    private JwtParser cachedParser;

    // ================================ 核心方法 ================================…


    /**
     * 生成JWT令牌（将对象序列化为JSON存入data字段）
     *
     * @param data              数据对象
     * @param expirationSeconds 过期时间（秒）
     * @param <T>               数据类型
     * @return JWT令牌字符串
     */
    public <T> String generateToken(String id, T data, long expirationSeconds) {
        try {
            String jsonData = JsonUtils.getObjectMapper().writeValueAsString(data);
            Map<String, Object> claims = Map.of(DATA_FIELD, jsonData);

            Date expirationDate = Date.from(Instant.now().plusSeconds(expirationSeconds));
            SecretKey key = getHmacShaKey();

            return Jwts.builder()
                    .header()
                    .type(JWT_HEADER_TYPE)
                    .and()
                    .id(id)
                    .expiration(expirationDate)
                    .issuedAt(Date.from(Instant.now()))
                    .issuer(issuer)
                    .claims(claims)
                    .signWith(key)
                    .compact();

        } catch (Exception e) {
            log.error("生成JWT令牌失败: data={}, error={}", data, e.getMessage(), e);
            throw new JwtParseException("生成JWT令牌失败", e);
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
    public <T> JwtValue<T> parseToken(String token, Class<T> type) {
        if (token == null || token.trim().isEmpty()) {
            throw new JwtParseException("令牌不能为空");
        }
        try {
            Claims claims = getJwtParser().parseSignedClaims(token).getPayload();
            Object data = claims.get(DATA_FIELD);
            if (data == null) {
                throw new JwtParseException("令牌中缺少data字段");
            }
            T value = JsonUtils.getObjectMapper().readValue(data.toString(), type);
            return new JwtValue<>(claims.getId(), value);

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

    // ================================ 私有方法 ================================

    /**
     * 获取签名密钥（带缓存）
     */
    private SecretKey getHmacShaKey() {
        if (cachedKey == null) {
            String fullKey = SECRET_KEY_PREFIX + "-" + secretKey;
            cachedKey = Keys.hmacShaKeyFor(fullKey.getBytes(StandardCharsets.UTF_8));
        }
        return cachedKey;
    }

    /**
     * 获取JWT解析器（带缓存）
     */
    private JwtParser getJwtParser() {
        if (cachedParser == null) {
            cachedParser = Jwts.parser().verifyWith(getHmacShaKey()).build();
        }
        return cachedParser;
    }
}