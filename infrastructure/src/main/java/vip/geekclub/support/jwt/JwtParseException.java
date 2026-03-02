package vip.geekclub.support.jwt;

import vip.geekclub.framework.exception.AppException;

/**
 * JWT解析异常
 */
public class JwtParseException  extends AppException {
    public JwtParseException(String message, Throwable cause) {
        super(401, message, cause);
    }

    public JwtParseException(String message) {
        super(401, message, null);
    }
}
