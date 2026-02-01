package vip.geekclub.framework.command;

/**
 * 命令总线接口
 * 仅定义应用层对外暴露的统一命令入口。
 */
public interface CommandBus {

    /**
     * 分发命令到对应的处理器
     */
    <C extends Command,R> CommandResult<R> dispatch(C command);
}
