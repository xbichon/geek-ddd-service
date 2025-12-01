package vip.geekclub.common.command;

import org.springframework.lang.NonNull;

/**
 * 命令执行结果类
 */
public record CommandResult<T>(String message, T primaryKey) {

    /**
     * 默认成功消息
     */
    private static final String DEFAULT_SUCCESS_MESSAGE = "操作成功";

    /**
     * 构造函数
     */
    public CommandResult {
    }

    /**
     * 创建仅包含消息的成功结果
     */
    public static CommandResult<Void> ok(String message) {
        return new CommandResult<>(message, null);
    }

    /**
     * 创建默认成功结果@
     */
    public static CommandResult<Void> ok() {
        return new CommandResult<>(DEFAULT_SUCCESS_MESSAGE, null);
    }

    /**
     * 创建包含消息和主键的成功结果
     */
    public static <T> CommandResult<T> ok(String message, T primaryKey) {
        return new CommandResult<>(message, primaryKey);
    }

    /**
     * 创建仅包含主键的成功结果
     */
    public static <T> CommandResult<T> ok(T primaryKey) {
        return new CommandResult<>(DEFAULT_SUCCESS_MESSAGE, primaryKey);
    }

    @Override
    public  @NonNull String toString() {
        return "CommandResult{" +
                "message='" + message + '\'' +
                ", primaryKey=" + primaryKey +
                '}';
    }
}