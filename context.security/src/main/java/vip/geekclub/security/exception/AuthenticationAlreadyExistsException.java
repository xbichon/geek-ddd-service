package vip.geekclub.security.exception;

public class AuthenticationAlreadyExistsException extends RuntimeException {
    public AuthenticationAlreadyExistsException(String message) {
        super(message);
    }
}