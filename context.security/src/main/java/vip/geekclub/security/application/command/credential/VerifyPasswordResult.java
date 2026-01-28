package vip.geekclub.security.application.command.credential;

public record VerifyPasswordResult(
        String authId,
        String userType
) {
}
