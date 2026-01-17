package vip.geekclub.common.exception;

import lombok.Getter;

@Getter
public class NotFoundException extends BusinessException {
    public NotFoundException(String message) {
        super(404, message);
    }
}