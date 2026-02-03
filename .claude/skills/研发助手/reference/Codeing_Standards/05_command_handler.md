# 一、命令处理器编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.application.command.{业务模块}` 包下；
- 类名使用 `{动作}{对象}CommandHandler` 命名，如 `CreateUserCommandHandler`；
- 必须实现 `vip.geekclub.framework.command.CommandHandler<C, R>` 接口；
- 使用 `@Service` 注解标记；
- 使用 Lombok 的 `@AllArgsConstructor` 注解进行依赖注入；
- 方法必须标注 `@Transactional` 注解以保证事务一致性；
- 返回类型统一使用 `CommandResult<T>` ；
- 命令处理器负责协调领域对象完成业务逻辑，一个命令对应一个明确的业务意图。
- 

## 2、示例

```java
@AllArgsConstructor
@Service
public class CreatePermissionCommandHandler implements CommandHandler<CreatePermissionCommand, IdResult> {

    private final PermissionRepository permissionRepository;

    @Override
    @Transactional
    public CommandResult<IdResult> execute(CreatePermissionCommand command) {
        // 1. 验证权限编码不存在
        if (permissionRepository.existsByCode(command.code())) {
            throw new ValidationException("权限编码已存在");
        }

        // 2. 创建权限领域对象
        Permission permission = new Permission(
            command.name(),
            PermissionCode.of(command.code()),
            command.description(),
            command.permissionGroupId()
        );
        permissionRepository.save(permission);

        // 3. 返回权限ID
        return CommandResult.ok(permission.getId());
    }
}
```

## 3、编写原则

- **单一职责**：每个命令处理器只处理一个明确的业务操作；
- **事务边界**：命令处理器是事务的边界，确保业务操作的原子性；
- **参数校验**：在命令处理器中进行业务规则的校验；
- **返回结果**：使用 `CommandResult` 包装返回结果，成功时返回数据，失败时抛出异常；
- **领域协调**：命令处理器协调领域对象完成业务逻辑，但不应包含复杂的领域规则。