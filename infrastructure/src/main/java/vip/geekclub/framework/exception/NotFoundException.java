package vip.geekclub.framework.exception;

import lombok.Getter;

/**
 * 资源未找到异常
 */
@Getter
public class NotFoundException extends AppException {
    public NotFoundException(String message) {
        super(404, message);
    }
}