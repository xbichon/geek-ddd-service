//package vip.geekclub.framework.command;
//
///**
// * 命令执行结果包装类（仅在需要时内部使用）
// * <p>
// * 注意：现在 CommandHandler 直接返回数据，不再使用 CommandResult 包装。
// * 此类保留用于向后兼容和特殊场景（如需要同时返回数据和消息）。
// *
// * @param message 执行消息
// * @param data    执行结果数据
// * @param <T>     数据类型
// */
//public record CommandResult<T>(String message, T data) {
//
//    /**
//     * 创建默认成功结果（无数据）
//     */
//    public static CommandResult<Void> ok() {
//        return new CommandResult<>("操作成功", null);
//    }
//
//    /**
//     * 创建包含数据的成功结果
//     */
//    public static <T> CommandResult<T> ok(T data) {
//        return new CommandResult<>("操作成功", data);
//    }
//
//    /**
//     * 创建包含自定义消息的成功结果
//     */
//    public static CommandResult<Void> ok(String message) {
//        return new CommandResult<>(message, null);
//    }
//
//    /**
//     * 创建包含自定义消息和数据的成功结果
//     */
//    public static <T> CommandResult<T> ok(String message, T data) {
//        return new CommandResult<>(message, data);
//    }
//}