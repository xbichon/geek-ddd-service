package vip.geekclub.framework.exception;

/**
 * 用户名密码验证错误异常
 */
public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException(String message) {
        super(401, message);
    }

    public InvalidCredentialsException(String message, Throwable cause) {
        super(401, message, cause);
    }
}