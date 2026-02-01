# 一、领域仓储编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.domain.repository` 包下,命名使用 `{领域模型}Repository` 命名，如 `UserRepository`；
- 采用Spring Data JPA的Repository模式；
- 优先考虑继承 `org.springframework.data.repository.CrudRepository<T, ID>`，需求不满足时再考虑 `org.springframework.data.repository.JpaRepository<T, ID>`；
- 实体类参数使用 `@NonNull` 注解标记；
- 类必须使用 `@Repository` 注解标记；
- 查询方法返回类型优先使用 `Optional<T>` 包装单条结果，集合返回使用 `List<T>`；
- **禁止过度设计**：没有明确的用户需求或其他业务模块调用需求的情况下，不要增加任何暂时用不到的仓库操作方法；
- 方法命名遵循 Spring Data JPA 的派生查询命名规范（如 `findByXxx`、`existsByXxx`、`countByXxx` 等）；
- 复杂的查询逻辑应通过应用层的 Query Handler 使用 JPA Criteria 或 QueryDSL 实现，而非在 Repository 中添加过多自定义方法。

## 2、示例

```java
package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.Principal;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrincipalRepository extends JpaRepository<@NonNull Principal, @NonNull Long> {

    /**
     * 根据外部用户ID查询用户
     *
     * @return 用户
     */
    Optional<Principal> findByAuthId(String authId);

    /**
     * 检查是否存在超级管理员
     *
     * @return 是否存在超级管理员
     */
    boolean existsByIsSuperAdminTrue();
}
```

## 3、方法添加原则

Repository 中的每一个方法都应该有明确的用途：

- **必须存在业务调用**：该方法至少有一个业务场景在使用；
- **避免「以防万一」**：不要预先添加「以后可能会用到」的方法；
- **及时清理**：如果方法不再被使用，应及时删除，而非注释掉。
