package vip.geekclub.framework.exception;

/**
 * 格式不正确异常
 */
public class InvalidFormatException extends BusinessException {

    public InvalidFormatException(String message) {
        super(400, message);
    }
}