package vip.geekclub.security.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 角色名称值对象
 * 用于存储角色的显示名称
 */
@Embeddable
public record RoleName(
    @Column(length = 40)
    String name
) {
    private static final int MAX_LENGTH = 40;
    
    public RoleName {
        // 角色名称验证
        name = StringUtils.trimToEmpty(name);
        if (StringUtils.isNotEmpty(name)) {
            AssertUtil.requireLengthLessThan(name, MAX_LENGTH + 1,
                    () -> "角色名称不能超过" + MAX_LENGTH + "个字符");
        }
    }
    
    public static RoleName of(String name) {
        return new RoleName(name);
    }
    
    public static RoleName empty() {
        return new RoleName("");
    }
    
    public boolean isEmpty() {
        return StringUtils.isEmpty(name);
    }
}
