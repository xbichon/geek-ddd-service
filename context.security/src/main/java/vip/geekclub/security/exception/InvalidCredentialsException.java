package vip.geekclub.security.exception;

import vip.geekclub.framework.exception.AppException;

/**
 * 用户名密码验证错误异常
 */
public class InvalidCredentialsException extends AppException {

    public InvalidCredentialsException(String message) {
        super(400, message);
    }
}