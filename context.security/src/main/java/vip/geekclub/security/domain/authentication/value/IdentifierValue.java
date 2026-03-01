package vip.geekclub.security.domain.authentication.value;

import jakarta.validation.constraints.NotBlank;

/**
 * 标识符值对象
 */

public record IdentifierValue(
        @NotBlank(message = "标识符类型不能为空") String type,
        @NotBlank(message = "标识符值不能为空") String value
) {

    public IdentifierValue {
        value = value.trim();
        type = type.trim().toLowerCase();
    }

    public static IdentifierValue ofUsername(String username) {
        return new IdentifierValue(IdentifierType.USERNAME, username);
    }

    public static IdentifierValue ofEmail(String email) {
        return new IdentifierValue(IdentifierType.EMAIL, email);
    }

    public static IdentifierValue ofPhone(String phone) {
        return new IdentifierValue(IdentifierType.PHONE, phone);
    }

}