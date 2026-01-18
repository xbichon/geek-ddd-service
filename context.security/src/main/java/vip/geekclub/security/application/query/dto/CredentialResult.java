package vip.geekclub.security.application.query.dto;

import lombok.Builder;
import vip.geekclub.security.domain.valueobject.AuthenticationType;
import vip.geekclub.security.domain.valueobject.UserType;

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