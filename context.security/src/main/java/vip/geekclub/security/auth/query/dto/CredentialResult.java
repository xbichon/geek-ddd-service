package vip.geekclub.security.auth.query.dto;

import lombok.Builder;
import vip.geekclub.security.auth.common.AuthenticationType;
import vip.geekclub.security.auth.common.UserType;

@Builder
public record CredentialResult(
        Long id,
        String identifier,
        String password,
        AuthenticationType type,
        Long userId,
        UserType userType
) {
}