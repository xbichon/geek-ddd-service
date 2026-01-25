package vip.geekclub.manager.application.port.dto;

import vip.geekclub.security.domain.value.CredentialType;

import java.util.UUID;

public record TeacherCredential(
        UUID authId,
        String identifier,
        String password,
        CredentialType credentialType
) {
}
