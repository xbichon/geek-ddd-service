package vip.geekclub.framework.exception;

/**
 * 业务逻辑异常
 */
public class BusinessLogicException extends BusinessException {
    public BusinessLogicException(String message) {
        super(500, message);
    }
}