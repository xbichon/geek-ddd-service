package vip.geekclub.security.permission.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;
import vip.geekclub.common.utils.AssertUtil;

/**
 * 权限码值对象
 * 用于存储权限的唯一标识码
 */
@Embeddable
public record PermissionCode(
    @Column(length = 50)
    String code
) {
    private static final int MAX_LENGTH = 50;
    private static final String CODE_REGEX = "^[A-Z_][A-Z0-9_]*$";
    
    public PermissionCode {
        // 权限码验证
        code = StringUtils.trimToEmpty(code);
        if (StringUtils.isNotEmpty(code)) {
            AssertUtil.requireLengthLessThan(code, MAX_LENGTH + 1,
                    () -> "权限码不能超过" + MAX_LENGTH + "个字符");
            AssertUtil.isTrue(code.matches(CODE_REGEX), 
                    () -> "权限码格式不正确，必须以大写字母或下划线开头，只能包含大写字母、数字、下划线");
        }
    }
    
    public static PermissionCode of(String code) {
        return new PermissionCode(code);
    }
    
    public static PermissionCode empty() {
        return new PermissionCode("");
    }
    
    public boolean isEmpty() {
        return StringUtils.isEmpty(code);
    }
}
