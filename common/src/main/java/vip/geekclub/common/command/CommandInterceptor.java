package vip.geekclub.common.command;

/**
 * 命令拦截器接口
 * <p>
 * 在DDD架构中，命令拦截器提供了AOP（面向切面编程）的能力，用于处理横切关注点。
 * 拦截器按照注册顺序执行beforeHandle，按照相反顺序执行afterHandle和onError。
 * <p>
 * 常见的使用场景包括：
 * <ul>
 * <li>日志记录和审计</li>
 * <li>权限验证和安全检查</li>
 * <li>性能监控和指标收集</li>
 * <li>事务管理</li>
 * <li>缓存处理</li>
 * </ul>
 * 
 * @author DDD Framework
 * @since 1.0
 */
public interface CommandInterceptor {

    /**
     * 命令处理前的拦截方法
     * <p>
     * 在命令处理器执行之前调用，可以用于参数验证、权限检查、日志记录等。
     * 如果此方法抛出异常，命令处理将被中断，不会执行后续的拦截器和命令处理器。
     *
     * @param command 即将被处理的命令对象
     * @throws RuntimeException 当前置检查失败时，可以抛出异常中断命令执行
     */
    default void beforeHandle(Command command) {
        // 默认空实现，子类可根据需要重写
    }

    /**
     * 命令处理后的拦截方法
     * <p>
     * 在命令处理器成功执行后调用，可以用于结果处理、缓存更新、事件发布等。
     * 此方法按照拦截器注册的相反顺序执行。
     *
     * @param command 已处理的命令对象
     * @param result  命令处理的结果
     * @param <T>     结果中主键的类型
     */
    default <T> void afterHandle(Command command, CommandResult<T> result) {
        // 默认空实现，子类可根据需要重写
    }

    /**
     * 命令处理异常时的拦截方法
     * <p>
     * 当命令处理过程中发生异常时调用，可以用于异常日志记录、清理资源、回滚操作等。
     * 此方法按照拦截器注册的相反顺序执行，只对已经执行过beforeHandle的拦截器调用。
     * <p>
     * 注意：此方法中抛出的异常不会覆盖原始异常，原始异常仍会向上传播。
     *
     * @param command   发生异常的命令对象
     * @param exception 处理过程中发生的异常
     */
    default void onError(Command command, Exception exception) {
        // 默认空实现，子类可根据需要重写
    }
}