package vip.geekclub.security.auth.application.query.dto;

import lombok.Builder;
import vip.geekclub.security.auth.domain.AuthenticationType;
import vip.geekclub.security.auth.domain.UserType;

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