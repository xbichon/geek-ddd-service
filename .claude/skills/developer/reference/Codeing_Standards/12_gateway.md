# 一、防腐层（Gateway）编写规范

## 1、规范

### 1.1 接口定义（防冲突命名）

**格式**：`{BoundedContext}{Domain}Gateway`

- `{BoundedContext}`：限界上下文名，如 `Internship`、`Manager`
- `{Domain}`：通用域名，如 `Security`、`Payment`、`Notification`

**示例**：
```java
// internship 模块调用 security 域
public interface InternshipSecurityGateway {
    void createStudentPrincipal(String authId, String studentNo, String password);
}

// manager 模块调用 security 域
public interface ManagerSecurityGateway {
    void createAdminPrincipal(String authId, String username, String password);
}
```

### 1.2 接口实现

**格式**：`{BoundedContext}{Domain}GatewayImpl`

```java
@Component
@RequiredArgsConstructor
public class InternshipSecurityGatewayImpl implements InternshipSecurityGateway {
    private final SecurityClient securityClient;

    @Override
    public void createStudentPrincipal(String authId, String studentNo, String password) {
        securityClient.createPrincipal(authId, studentNo, password, UserType.STUDENT);
    }
}
```

### 1.3 放置规范

- **接口**：`vip.geekclub.{boundedContext}.application.gateway`
- **实现**：`vip.geekclub.{boundedContext}.adapter.gateway`

```
internship模块
├── application/gateway/InternshipSecurityGateway.java  (接口)
└── adapter/gateway/InternshipSecurityGatewayImpl.java  (实现)

manager模块
├── application/gateway/ManagerSecurityGateway.java     (接口)
└── adapter/gateway/ManagerSecurityGatewayImpl.java     (实现)
```

## 2、设计原则

### 3.1 防腐层保护调用方

- 定义在**调用方模块**内部，而非被调用方
- 被调用方只提供应用服务或 Command/Query

### 3.2 严禁直接使用通用域 Client

必须通过本模块的 Gateway 接口，防止直接依赖：

```java
// 错误：直接依赖通用域
@Autowired private SecurityClient securityClient;

// 正确：通过本模块 Gateway
@Autowired private InternshipSecurityGateway securityGateway;
```

### 3.3 接口与实现分离

```
application.gateway/     ← 接口定义（契约）
adapter.gateway/         ← 接口实现（技术细节）
```

## 4、常见命名对照

| 通用域 | internship 模块 | manager 模块 |
|:---|:---|:---|
| Security | InternshipSecurityGateway | ManagerSecurityGateway |
| Payment | InternshipPaymentGateway | ManagerPaymentGateway |
| Notification | InternshipNotificationGateway | ManagerNotificationGateway |
| Storage | InternshipStorageGateway | ManagerStorageGateway |

## 5、为什么不使用 @Qualifier？

通过类名区分（推荐）：
```java
// 编译期强制校验，无需额外说明
private final InternshipSecurityGateway gateway;
```

使用 @Qualifier 的写法（不推荐）：
```java
// 容易遗忘，可读性差
@Autowired @Qualifier("internship")
private SecurityGateway gateway;
```