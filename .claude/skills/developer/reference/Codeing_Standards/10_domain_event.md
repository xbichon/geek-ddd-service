# 一、领域事件编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.domain.event` 包下；
- 类名使用 `{对象}{动作}Event` 命名，如 `UserCreatedEvent`、`OrderPaidEvent`；
- 必须实现 `vip.geekclub.framework.domain.event.DomainEvent` 标记接口；
- 使用 Java Record 定义，确保事件的不可变性；
- 包含事件发生时的关键数据，用于后续处理；
- 事件字段应包含足够的信息，避免订阅者需要再次查询。

## 2、示例

```java
package vip.geekclub.manager.domain.event;

import vip.geekclub.framework.domain.event.DomainEvent;
import java.util.UUID;

/**
 * 用户创建事件
 * 当新用户被创建时触发
 */
public record UserCreatedEvent(
    Long id,
    String email,
    String phone,
    UUID externalUuid
) implements DomainEvent {
}
```

### 事件发布示例

```java
// 在领域模型或命令处理器中发布事件
public class User {

    public void activate() {
        this.status = UserStatus.ACTIVE;
        this.activatedAt = LocalDateTime.now();

        // 发布领域事件
        DomainEventPublisher.publish(new UserActivatedEvent(this.id, this.activatedAt));
    }
}
```

### 事件订阅示例

```java
@Component
public class UserEventListener {

    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        // 发送欢迎邮件
        emailService.sendWelcomeEmail(event.email());
    }
}
```

## 3、设计原则

- **不可变性**：使用 Record 确保事件一旦创建不可修改；
- **过去时态**：事件名使用过去时态，表示已发生的事实，如 `Created`、`Paid`、`Shipped`；
- **自包含性**：事件包含订阅者处理所需的全部数据，减少查询依赖；
- **最终一致性**：领域事件用于实现跨聚合的最终一致性；
- **解耦**：通过事件实现模块间的解耦，降低系统耦合度。