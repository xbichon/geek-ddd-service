package vip.geekclub.common.command;

import vip.geekclub.common.utils.AssertUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命令总线默认实现
 * <p>
 * 在DDD架构中，命令总线是应用服务层的核心组件，负责接收命令并将其路由到相应的命令处理器。
 * 它提供了统一的命令处理入口，支持拦截器机制来处理横切关注点。
 * <p>
 * 主要功能：
 * <ul>
 * <li>自动发现和注册命令处理器</li>
 * <li>命令路由和分发</li>
 * <li>命令验证</li>
 * <li>拦截器链管理</li>
 * <li>异常处理和传播</li>
 * </ul>
 */
public class SimpleCommandBus implements CommandBus {

    /**
     * 错误消息常量
     */
    private static final String NO_HANDLER_ERROR_MSG = "没有为命令类型注册命令处理器: %s";

    /**
     * 命令类型到处理器的映射
     */
    private final Map<Class<? extends Command>, CommandHandler<?, ?>> commandHandlers ;

    /**
     * 命令拦截器列表，按注册顺序执行
     */
    private final List<CommandInterceptor> interceptors ;

    /**
     * 私有构造器，只能通过 Builder 创建
     */
    private SimpleCommandBus(Builder builder) {
        this.commandHandlers = new HashMap<>(builder.commandHandlers);
        this.interceptors = new ArrayList<>(builder.interceptors);
    }

    /**
     * 创建 Builder 实例
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * 分发命令到对应的处理器
     */
    @Override
    public <C extends Command, T> CommandResult<T> dispatch(C command) {
        // 1. 命令非空验证
        AssertUtil.notNull(command, () -> "命令不能为空(" + command.getClass().getName() + ")");

        // 2. 查找命令处理器
        CommandHandler<C, T> handler = findHandler(command);

        // 3. 执行拦截器链和命令处理
        return executeWithInterceptors(command, handler);
    }


    /**
     * 查找命令对应的处理器
     */
    @SuppressWarnings("unchecked")
    private <C extends Command, T> CommandHandler<C, T> findHandler(C command) {

        CommandHandler<?, ?> commandHandler = commandHandlers.get(command.getClass());
        if (commandHandler == null) {
            throw new IllegalStateException(String.format(NO_HANDLER_ERROR_MSG, command.getClass().getName()));
        }

        return  (CommandHandler<C, T>) commandHandler;
    }

    /**
     * 执行拦截器链和命令处理
     */
    private <C extends Command, T> CommandResult<T> executeWithInterceptors(C command, CommandHandler<C, T> commandHandler) {
        int beforeCount = 0; // 记录成功执行过 before 的拦截器数量
        try {
            // 1) 前置拦截器（按注册顺序执行）
            for (; beforeCount < interceptors.size(); beforeCount++) {
                interceptors.get(beforeCount).beforeHandle(command);
            }

            // 2) 执行命令处理器
            CommandResult<T> result = commandHandler.execute(command);

            // 3) 后置拦截器（按相反顺序执行）
            for (int i = beforeCount - 1; i >= 0; i--) {
                interceptors.get(i).afterHandle(command, result);
            }

            return result;
        } catch (Exception ex) {
            // 4) 异常时：仅对已成功执行 before 的拦截器按相反顺序执行 onError
            for (int i = beforeCount - 1; i >= 0; i--) {
                interceptors.get(i).onError(command, ex);
            }
            throw ex;
        }
    }


    /**
     * SimpleCommandBus 的 Builder 类
     */
    public static class Builder {
        private final Map<Class<? extends Command>, CommandHandler<?, ?>> commandHandlers = new HashMap<>();
        private final List<CommandInterceptor> interceptors = new ArrayList<>();

        /**
         * 添加单个命令处理器
         */
        private void addHandler(CommandHandler<?, ?> handler) {
            AssertUtil.notNull(handler, () -> "CommandHandler 不能为空");
            Class<? extends Command> commandType = handler.getCommandType();

            CommandHandler<?, ?> existing = this.commandHandlers.put(commandType, handler);
            if (existing != null) {
                throw new IllegalStateException("检测到重复的 CommandHandler 注册: " + commandType.getName());
            }
        }

        /**
         * 批量添加命令处理器
         */
        public Builder addHandlers(List<CommandHandler<?, ?>> handlers) {
            AssertUtil.notNull(handlers, () -> "CommandHandler 列表不能为空");
            for (CommandHandler<?, ?> handler : handlers) {
                addHandler(handler);
            }
            return this;
        }

        /**
         * 添加单个拦截器
         */
        public Builder addInterceptor(CommandInterceptor interceptor) {
            AssertUtil.notNull(interceptor, () -> "CommandInterceptor 不能为空");
            this.interceptors.add(interceptor);
            return this;
        }

        /**
         * 构建 SimpleCommandBus 实例
         */
        public SimpleCommandBus build() {
            return new SimpleCommandBus(this);
        }
    }
}