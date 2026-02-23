package vip.geekclub.framework.command;

/**
 * 命令总线接口
 * 仅定义应用层对外暴露的统一命令入口。
 */
public interface CommandBus {

    /**
     * 分发命令到对应的处理器
     * <p>
     * 返回类型由命令自身的泛型参数决定，是命令执行的自然产出：
     * - 计算型命令：返回计算结果
     * - 创建型命令：返回生成的标识
     * - 删除/更新型命令：返回 Void（即 null）
     *
     * @param command 要执行的命令
     * @param <R>     命令返回类型（由 Command<R> 推断）
     * @return 命令执行的自然产出结果
     */
    <R> R dispatch(Command<R> command);
}
