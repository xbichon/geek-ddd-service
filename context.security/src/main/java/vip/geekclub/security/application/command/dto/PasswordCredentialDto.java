package vip.geekclub.security.application.command.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record PasswordCredentialDto(
        @Valid
        @NotEmpty(message = "标识符不能为空")
        @Size(min = 1, max = 3, message = "最多只能添加3个标识符")
        List<IdentifierDto> identifiers,

        @NotBlank(message = "密码不能为空")
        @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
        String password
) {
}