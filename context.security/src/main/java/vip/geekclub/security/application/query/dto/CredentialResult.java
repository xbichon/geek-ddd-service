package vip.geekclub.security.application.query.dto;

import lombok.Builder;
import vip.geekclub.security.domain.value.CredentialType;
import vip.geekclub.security.domain.value.UserType;

@Builder
public record CredentialResult(
        Long id,
        String identifier,
        String password,
        CredentialType type,
        Long userId,
        String userType
) {
}