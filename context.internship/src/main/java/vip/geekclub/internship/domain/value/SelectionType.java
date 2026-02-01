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

    /**
     * 获取最小选择者数量
     *
     * @return 最小选择者数量
     */
    public int getMinSelections() {
        return this == INDIVIDUAL ? 1 : 2;
    }

    /**
     * 获取最大选择者数量
     *
     * @return 最大选择者数量
     */
    public int getMaxSelections() {
        return this == INDIVIDUAL ? 1 : 5;
    }

    /**
     * 验证选择者数量是否合法
     *
     * @param count 选择者数量
     * @return 是否合法
     */
    public boolean isValidSelectionCount(int count) {
        return count >= getMinSelections() && count <= getMaxSelections();
    }
}
