package vip.geekclub.common.command;

/**
 * 命令执行结果类
 */
public record CommandResult<T>(String message, T data) {

    /**
     * 创建默认成功结果@
     */
    public static CommandResult<Void> ok() {
        return new CommandResult<>("操作成功", null);
    }

    /**
     * 创建包含消息和主键的成功结果
     */
    public static CommandResult<IdResult> ok(Long primaryKey) {
        return new CommandResult<>("操作成功", new IdResult(primaryKey));
    }

    /**
     * 创建包含消息和主键的成功结果
     */
    public static CommandResult<Void> ok(String message) {
        return new CommandResult<>(message, null);
    }
}