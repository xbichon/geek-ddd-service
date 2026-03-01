package vip.geekclub.security.domain.value;

import jakarta.validation.constraints.NotBlank;

/**
 * 标识符值对象
 */

public record IdentifierValue(
        @NotBlank(message = "标识符类型不能为空") String type,
        @NotBlank(message = "标识符值不能为空") String value
) {

    public final static String EMAIL = "email";
    public final static String PHONE = "phone";
    public final static String USERNAME = "username";

    public IdentifierValue {
        value = value.trim();
        type = type.trim().toLowerCase();
    }

    public static IdentifierValue ofUsername(String username) {
        return new IdentifierValue(USERNAME, username);
    }

    public static IdentifierValue ofEmail(String email) {
        return new IdentifierValue(EMAIL, email);
    }

    public static IdentifierValue ofPhone(String phone) {
        return new IdentifierValue(PHONE, phone);
    }

}