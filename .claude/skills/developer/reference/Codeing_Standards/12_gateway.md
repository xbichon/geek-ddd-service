# 一、防腐层（Gateway）编写规范

## 1、规范

### 1.1 接口定义

- 文件放置在 `application.gateway` 包下；
- 接口名使用 `{外部模块名}Gateway` 命名，如 `SecurityGateway`；
- 方法命名使用领域友好术语，屏蔽外部模块细节；
- DTO 定义在 `application.gateway.dto` 子包下。

### 1.2 接口实现

- 文件放置在 `adapter.gateway` 包下；
- 实现类使用 `{接口名}Impl` 命名，如 `SecurityGatewayImpl`；
- 使用 `@Component` 注解标记；
- 负责调用外部模块、异常转换、数据映射。

### 1.3 放置原则

- **默认放在应用层**：由 Command Handler 或应用服务调用；
- **下沉到领域层的条件**：必须是通用业务概念，且领域实体必须直接调用。

## 2、示例

### 2.1 接口定义

```java
public interface SecurityGateway {
    Long createPrincipal(String authId, List<Identifier> identifiers,
                         String password, Set<Long> roles);
    void deletePrincipal(String authId);
}
```

### 2.2 接口实现

```java
@Component
@RequiredArgsConstructor
public class SecurityGatewayImpl implements SecurityGateway {

    private final CommandBus commandBus;

    @Override
    public Long createPrincipal(String authId, List<Identifier> identifiers,
                                String password, Set<Long> roles) {
        // 转换并调用外部模块
        var cmd = new CreatePrincipalCommand(authId, identifiers, password, roles);
        return commandBus.dispatch(cmd);
    }

    @Override
    public void deletePrincipal(String authId) {
        commandBus.dispatch(new DeletePrincipalCommand(authId));
    }
}
```

### 2.3 使用方式

```java
@Service
@RequiredArgsConstructor
public class CreateTeacherHandler implements CommandHandler<CreateTeacherCommand, Long> {

    private final TeacherRepository repository;
    private final SecurityGateway securityGateway;  // 注入防腐层

    @Override
    public Long execute(CreateTeacherCommand cmd) {
        // 1. 创建领域对象
        Teacher teacher = Teacher.create(cmd.name(), cmd.email());
        repository.save(teacher);

        // 2. 通过防腐层调用外部模块
        securityGateway.createPrincipal(
            teacher.getAuthId(),
            List.of(new Identifier("email", cmd.email())),
            "123456",
            cmd.roleIds()
        );

        return teacher.getId();
    }
}
```

## 3、设计原则

### 3.1 防腐层保护调用方

- 定义在**调用方模块**内部，而非被调用方；
- 被调用方只提供应用服务或 Command/Query。

### 3.2 接口与实现分离

```
application.gateway/     ← 接口定义（契约）
    └── SecurityGateway.java

adapter.gateway/         ← 接口实现（技术细节）
    └── SecurityGatewayImpl.java
```

### 3.3 放置原则

- **默认放在应用层**，由应用服务调用；
- **下沉到领域层的条件**（必须同时满足）：
  1. 换技术实现概念仍成立；
  2. 领域实体必须直接调用；
  3. 无法通过参数传入解决。
