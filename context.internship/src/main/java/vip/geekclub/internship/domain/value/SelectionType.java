package vip.geekclub.internship.domain.value;

/**
 * 选择者类型枚举
 * 定义选题记录的选择方式
 */
public enum SelectionType {
    /**
     * 个人
     * 个人选题时，只能有一个选择者
     */
    INDIVIDUAL,

    /**
     * 组
     * 组选题时，可以有2-5个选择者
     */
    GROUP;
}
