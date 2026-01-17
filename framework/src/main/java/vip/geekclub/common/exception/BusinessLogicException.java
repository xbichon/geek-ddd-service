package vip.geekclub.common.exception;

public class BusinessLogicException extends BusinessException {
    public BusinessLogicException(String message) {
        super(500, message);
    }
}