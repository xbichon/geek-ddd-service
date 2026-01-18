package vip.geekclub.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 描述值对象
 * 用于存储各种实体的描述信息
 */
@Embeddable
public record Description(
    @Column(length = 100)
    String description
) {
    private static final int MAX_LENGTH = 100;
    
    public Description {
        // 描述验证
        description = StringUtils.trimToEmpty(description);
        if (StringUtils.isNotEmpty(description)) {
            AssertUtil.requireLengthLessThan(description, MAX_LENGTH + 1,
                    () -> "描述长度不能超过" + MAX_LENGTH + "个字符");
        }
    }
    
    public static Description of(String description) {
        return new Description(description);
    }
    
    public static Description empty() {
        return new Description("");
    }
    
    public boolean isEmpty() {
        return StringUtils.isEmpty(description);
    }
}
