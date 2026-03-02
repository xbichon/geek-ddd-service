package vip.geekclub.framework.exception;

/**
 * 验证异常
 */
public class ValidationException extends AppException {
    public ValidationException(String message) {
        super( 400,message);
    }

    public ValidationException(String message, Throwable cause) {
        super(400,message, cause);
    }
}