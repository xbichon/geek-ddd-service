# 一、命令处理器编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.application.command.{业务模块}` 包下；
- 类名使用 `{动作}{对象}CommandHandler` 命名，如 `CreateUserCommandHandler`；
- 必须实现 `vip.geekclub.framework.command.CommandHandler<C extends Command<R>, R>` 接口，C为命令类型，R为返回类型；
- 使用 `@Service` 注解标记，无需 `@CommandHandlerMapping`（由命令指定）；
- 使用 Lombok 的 `@AllArgsConstructor` 注解进行依赖注入；
- 方法必须标注 `@Transactional` 注解以保证事务一致性；
- 返回类型为 R（命令泛型指定的类型），无需包装；
- 命令处理器负责协调领域对象完成业务逻辑，一个命令对应一个明确的业务意图；
- 命令端禁止调用查询端代码，保持 CQRS 架构隔离；
- **返回值尽量精简，优先返回最小标识（如ID），复杂数据交由查询端处理，保持 CQRS 读写分离**；

## 2、示例

```java
@Service
@AllArgsConstructor
public class CreateUserCommandHandler implements CommandHandler<CreateUserCommand, Long> {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public Long execute(CreateUserCommand command) {
        // 1. 创建用户（命令端只返回ID）
        User user = new User(command.username(), command.password());
        userRepository.save(user);

        // 2. 返回最小标识
        return user.getId();
    }
}
```

## 3、编写原则

- **单一职责**：每个命令处理器只处理一个明确的业务操作；
- **事务边界**：命令处理器是事务的边界，确保业务操作的原子性；
- **参数校验**：在命令处理器中进行业务规则的校验；
- **返回结果**：直接返回命令泛型指定的类型 R，成功返回数据，失败抛出异常；
- **领域协调**：命令处理器协调领域对象完成业务逻辑，但不应包含复杂的领域规则。