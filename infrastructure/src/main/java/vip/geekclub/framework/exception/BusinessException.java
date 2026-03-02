package vip.geekclub.framework.exception;

/**
 * 业务逻辑异常
 */
public class BusinessException extends AppException {
    public BusinessException(String message) {
        super(422, message);
    }
}