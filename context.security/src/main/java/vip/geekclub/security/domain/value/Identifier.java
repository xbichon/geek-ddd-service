package vip.geekclub.security.domain.value;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Size;
import lombok.*;

@Embeddable
@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Identifier {

    /**
     * 标识符类型
     */
    private IdentifierType type;

    /**
     * 标识符值
     */
    @Size(min = 1, max = 200, message = "标识符长度必须在1-200个字符之间")
    private String value;
}
