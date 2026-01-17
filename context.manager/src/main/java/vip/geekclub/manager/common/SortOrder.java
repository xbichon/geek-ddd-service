package vip.geekclub.manager.common;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import vip.geekclub.common.utils.AssertUtil;

/**
 * 排序号值对象
 */
@Embeddable
public record SortOrder(@Column(name = "sort_order") @JsonValue Integer sortOrder) {

    private static final int DEFAULT_VALUE = 99;
    public static final int MIN_VALUE = 0;
    public static final int MAX_VALUE = 99;

    public SortOrder {
        AssertUtil.notNull(sortOrder, () -> "排序号不能为空");
        AssertUtil.isTrue(sortOrder >= MIN_VALUE && sortOrder <= MAX_VALUE,
                () -> "排序号必须在0-99之间");
    }

    public static SortOrder of(Integer sortOrder) {
        return new SortOrder(sortOrder);
    }

    public static SortOrder defaultOrder() {
        return new SortOrder(DEFAULT_VALUE);
    }
}
