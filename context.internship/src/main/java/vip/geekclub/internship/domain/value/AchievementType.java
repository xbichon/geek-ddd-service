package vip.geekclub.internship.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import org.apache.commons.lang3.StringUtils;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 成果形式值对象
 * 用于定义论文的成果形式
 */
@Embeddable
public record AchievementType(
    @Column(length = 50)
    String type
) {
    private static final int MAX_LENGTH = 50;

    public AchievementType {
        type = StringUtils.trimToEmpty(type);
        if (StringUtils.isNotEmpty(type)) {
            AssertUtil.requireLengthLessThan(type, MAX_LENGTH + 1,
                    () -> "成果形式长度不能超过" + MAX_LENGTH + "个字符");
        }
    }

    public static AchievementType of(String type) {
        return new AchievementType(type);
    }

    public static AchievementType empty() {
        return new AchievementType("");
    }

    public boolean isEmpty() {
        return StringUtils.isEmpty(type);
    }
}
