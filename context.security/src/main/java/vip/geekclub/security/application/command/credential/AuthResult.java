package vip.geekclub.security.application.command.credential;

/**
 * 认证结果
 */
public record AuthResult(
        String authId,
        String userType
) {
}
