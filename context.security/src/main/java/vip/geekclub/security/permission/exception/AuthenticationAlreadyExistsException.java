package vip.geekclub.security.permission.exception;

public class AuthenticationAlreadyExistsException extends RuntimeException {
    public AuthenticationAlreadyExistsException(String message) {
        super(message);
    }
}