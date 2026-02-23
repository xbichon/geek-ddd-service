package vip.geekclub.framework.command;

/**
 * 无返回值命令处理器接口
 * <p>
 * 专门用于处理不需要返回业务数据的命令（如删除、更新等操作）。
 * 继承自 {@link CommandHandler}，但将返回值固定为 {@link Void}，
 * 并提供 {@link #executeVoid(Command)} 方法让子类实现，避免在代码中写 {@code return null}。
 * <p>
 * 实现类只需实现 {@link #executeVoid(Command)} 方法，无需处理返回值。
 *
 * <p>使用示例：</p>
 * <pre>
 * @Service
 * public class DeleteTeacherCommandHandler
 *         implements VoidCommandHandler<DeleteTeacherCommand> {
 *
 *     @Override
 *     public void executeVoid(DeleteTeacherCommand command) {
 *         teacherRepository.deleteById(command.id());
 *         // 无需 return 语句
 *     }
 * }
 * </pre>
 *
 * @param <C> 此处理器可以处理的命令类型（必须是 Command<Void>）
 * @author DDD Framework
 * @since 1.0
 * @see CommandHandler
 */
public interface VoidCommandHandler<C extends Command<Void>> extends CommandHandler<C, Void> {

    /**
     * 处理指定的命令（无返回值版本）
     * <p>
     * 子类只需实现此方法，无需关心 Void 类型的返回。
     *
     * @param command 要处理的命令对象
     */
    void executeVoid(C command);

    /**
     * 适配方法，将 executeVoid 包装成 CommandHandler 要求的返回值
     * <p>
     * 默认实现，子类不应覆盖此方法。
     *
     * @param command 要处理的命令对象
     * @return 始终返回 null（Void 类型的唯一值）
     */
    @Override
    default Void execute(C command) {
        executeVoid(command);
        return null;
    }
}
