package vip.geekclub.framework.exception;

public class JwtParseException  extends BusinessException{
    public JwtParseException(String message, Throwable cause) {
        super(401, message, cause);
    }

    public JwtParseException(String message) {
        super(401, message, null);
    }
}
