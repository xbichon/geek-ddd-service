# 一、命令数据传输对象（Command DTO）编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.application.command.{业务模块}` 包下，与对应的 CommandHandler 同包；
- 类名使用 `{动作}{对象}Command` 命名，如 `CreateUserCommand`；
- 必须实现 `vip.geekclub.framework.command.Command` 标记接口；
- 优先考虑使用 Java Record 定义，确保命令对象的不可变性；
- 使用 `jakarta.validation.constraints` 包下的注解进行参数校验；
- 在 compact constructor 中使用 `StringUtil.trimToNull()` 对字符串字段进行清理。

## 2、示例

```java
/**
 * 创建权限命令
 */
public record CreatePermissionCommand(

    String name,

    @NotBlank(message = "授权码不能为空")
    @Size(max = 50, message = "授权码长度不能超过50")
    String code,

    @Size(max = 100, message = "描述长度不能超过100")
    String description,

    Long permissionGroupId
) implements Command {

    public CreatePermissionCommand {
        // 字符串字段trim处理
        name = StringUtil.trimToNull(name);
        code = StringUtil.trimToNull(code);
        description = StringUtil.trimToNull(description);
    }
}
```

## 3、设计原则

- **不可变性**：使用 Record 确保命令对象一旦创建不可修改；
- **自描述性**：通过校验注解明确字段约束，无需额外文档说明；
- **轻量化**：Command DTO 只包含执行操作所需的数据，不包含业务逻辑；
- **字符串处理**：在构造函数中对字符串字段进行 trim 处理，避免存储无意义的前后空格；
- **验证优先**：使用 Bean Validation 注解在控制器层完成基础校验，减少无效请求进入业务层。