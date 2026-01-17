package vip.geekclub.common.command;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import org.springframework.core.ResolvableType;

/**
 * 命令处理器接口
 * <p>
 * 在DDD架构中，命令处理器负责执行具体的业务逻辑。每个命令处理器只处理一种特定类型的命令，
 * 遵循单一职责原则。命令处理器是应用服务层的核心组件，协调领域对象完成业务操作。
 * <p>
 * 实现类应该使用 {@code @Component} 注解标记，以便被Spring容器管理和自动注册到命令总线中。
 *
 * @param <C> 此处理器可以处理的命令类型
 * @author DDD Framework
 * @since 1.0
 */
public interface CommandHandler<C extends Command,R> {

    /**
     * 处理指定的命令
     * <p>
     * 此方法包含具体的业务逻辑实现，应该保证操作的原子性和一致性。
     * 如果处理过程中发生业务异常，应该抛出相应的领域异常。
     *
     * @param command 要处理的命令对象，包含执行业务操作所需的所有参数
     * @return 命令执行结果，包含操作状态信息和可选的业务主键
     * @throws RuntimeException 当业务规则验证失败或执行过程中发生错误时
     */
    CommandResult<R> execute(C command);

    /**
     * 获取此处理器能够处理的命令类型
     * <p>
     * 优先通过 Spring 的 ResolvableType 解析，避免直接取 getGenericInterfaces()[0] 带来的不稳定性。
     *
     * @return 命令类型的Class对象
     */
    @SuppressWarnings("unchecked")
    default Class<C> getCommandType() {
        // 优先使用更稳妥的解析方式
        ResolvableType type = ResolvableType.forClass(getClass()).as(CommandHandler.class);
        Class<?> resolved = type.getGeneric(0).resolve();
        return (Class<C>) Objects.requireNonNullElseGet(resolved
                , () -> ((ParameterizedType) getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0]);
    }
}