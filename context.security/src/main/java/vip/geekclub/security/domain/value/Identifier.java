package vip.geekclub.security.domain.value;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * 标识符值对象
 */
@Embeddable
public record Identifier(
        @NotBlank(message = "标识符值不能为空") String value,
        @NotNull(message = "标识符类型不能为空") IdentifierType type
) {}