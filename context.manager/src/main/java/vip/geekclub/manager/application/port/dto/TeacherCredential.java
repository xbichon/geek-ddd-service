package vip.geekclub.manager.application.port.dto;

import java.util.UUID;

public record TeacherCredential(
        UUID authId,
        String username,
        String password
) {
}
