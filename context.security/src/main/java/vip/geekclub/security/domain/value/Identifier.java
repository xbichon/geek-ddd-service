package vip.geekclub.security.domain.value;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.framework.exception.InvalidFormatException;

/**
 * 标识符值对象
 */
@Embeddable
public record Identifier(

        @NotNull(message = "标识符类型不能为空")
        @Enumerated(EnumType.STRING)
        IdentifierType type,
        @NotBlank(message = "标识符值不能为空") String value
) {

    public Identifier {
        if (type == IdentifierType.EMAIL && !isEmail(value)) {
            throw new InvalidFormatException("邮箱格式不正确");
        }
        if (type == IdentifierType.PHONE && !isPhone(value)) {
            throw new InvalidFormatException("手机号格式不正确");
        }
        if (type == IdentifierType.USERNAME && (isEmail(value) || isPhone(value))) {
            throw new InvalidFormatException("用户名不能是邮箱或手机号格式");
        }
    }

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";

    private static boolean isEmail(String value) {
        return value.matches(EMAIL_REGEX);
    }

    private static boolean isPhone(String value) {
        return value.matches(PHONE_REGEX);
    }
}