package vip.geekclub.framework.command;

import vip.geekclub.framework.utils.AssertUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 命令总线默认实现（基于责任链模式）
 * <p>
 * 在DDD架构中，命令总线是应用服务层的核心组件，负责接收命令并将其路由到相应的命令处理器。
 * 采用责任链模式（Chain of Responsibility）处理命令，提供了更大的灵活性和扩展性。
 * <p>
 * 主要功能：
 * <ul>
 * <li>自动发现和注册命令处理器</li>
 * <li>命令路由和分发</li>
 * <li>责任链构建和管理</li>
 * <li>拦截器适配和支持</li>
 * <li>异常处理和传播</li>
 * </ul>
 * <p>
 * 责任链结构：CommandMiddleware 链，由 InterceptorAdapter（适配原有拦截器）和
 * HandlerAdapter（适配命令处理器）组成。链的执行顺序保证与原有拦截器模式一致。
 */
public class SimpleCommandBus implements CommandBus {

    /**
     * 错误消息常量
     */
    private static final String NO_HANDLER_ERROR_MSG = "没有为命令类型注册命令处理器: %s";

    /**
     * 命令类型到处理器的映射
     */
    private final Map<Class<? extends Command>, CommandHandler<?, ?>> commandHandlers;

    /**
     * 责任链头节点，默认包含命令处理器执行逻辑
     */
    private CommandHandlerChain chain;

    /**
     * 构造函数，初始化命令处理器映射和责任链头节点
     */
    public SimpleCommandBus() {
        this.commandHandlers = new HashMap<>();
        this.chain = new CommandHandlerChain() {
            @Override
            @SuppressWarnings("unchecked")
            protected <R> CommandResult<R> handle(Command command, CommandHandlerChain chain) {
                CommandHandler<?, ?> commandHandler = commandHandlers.get(command.getClass());
                AssertUtil.notNull(commandHandler, () -> String.format(NO_HANDLER_ERROR_MSG, command.getClass().getName()));
                return ((CommandHandler<Command, R>) commandHandler).execute(command);
            }
        };
    }

    /**
     * 批量添加命令处理器
     */
    public void addHandlers(List<CommandHandler<?, ?>> handlers) {

        if (handlers == null || handlers.isEmpty()) return;

        for (CommandHandler<?, ?> handler : handlers) {
            Class<? extends Command> commandType = handler.getCommandType();
            CommandHandler<?, ?> existing = this.commandHandlers.put(commandType, handler);
            AssertUtil.isNull(existing, () -> "检测到重复的 CommandHandler 注册: " + commandType.getName());
        }
    }

    /**
     * 添加责任链节点
     */
    public void addChain(CommandHandlerChain chain) {
        chain.setNextChain(this.chain);
        this.chain = chain;
    }

    /**
     * 分发命令到对应的处理器
     */
    @Override
    public <C extends Command, R> CommandResult<R> dispatch(C command) {
        // 1. 命令非空验证
        AssertUtil.notNull(command, () -> "命令不能为空(" + command.getClass().getName() + ")");

        // 2. 执行责任链
        return chain.handle(command);
    }
}