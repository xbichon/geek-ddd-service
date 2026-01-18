package vip.geekclub.security.permission.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import vip.geekclub.framework.utils.AssertUtil;

/**
 * 排序值对象
 * 用于存储排序信息
 */
@Embeddable
public record SortOrder(
    @Column(name = "sort_order")
    Integer sortOrder
) {
    private static final Integer DEFAULT_VALUE = 0;
    private static final Integer MIN_VALUE = 0;
    private static final Integer MAX_VALUE = 100;

    public SortOrder {
        if (sortOrder == null) {
            sortOrder = DEFAULT_VALUE;
        }
        AssertUtil.isTrue(sortOrder >= MIN_VALUE && sortOrder <= MAX_VALUE,
                () -> "排序值必须在" + MIN_VALUE + "-" + MAX_VALUE + "之间");
    }

    public static SortOrder of(Integer sortOrder) {
        return new SortOrder(sortOrder);
    }

    public static SortOrder defaultOrder() {
        return new SortOrder(DEFAULT_VALUE);
    }
}
