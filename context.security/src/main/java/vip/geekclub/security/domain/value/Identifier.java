package vip.geekclub.security.domain.value;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

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
}