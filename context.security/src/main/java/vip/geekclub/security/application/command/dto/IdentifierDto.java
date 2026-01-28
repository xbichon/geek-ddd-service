package vip.geekclub.security.application.command.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.util.StringUtils;
import vip.geekclub.security.domain.value.IdentifierType;

/**
     * 标识符 DTO
     */
    public record IdentifierDto(
            @NotBlank(message = "标识符值不能为空") String value,
            @NotNull(message = "标识符类型不能为空") IdentifierType type
    ) {
        public IdentifierDto {
            value = StringUtils.trimAllWhitespace(value);
        }
    }