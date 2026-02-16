# 查询处理器编写规范

## 规范

- 文件放在 `vip.geekclub.{业务领域}.application.query` 包下；
- 类名：`{结果}QueryService`，如 `UserItemQueryService`；
- **一个查询结果对应一个服务**：列表用 `ItemQueryService`，详情用 `DetailQueryService`；
- 使用 `@Service` + `@AllArgsConstructor`；
- 注入 `DSLContext`，用 JOOQ 直接查数据库，不走领域层；
- 返回 DTO 或基本类型，不用 `ApiResponse`；
- **方法命名**：`findPage`（分页）、`findAll`（不分页）、`findById`（单个）；
- 查询不开启事务。

## 示例

```java
// 列表结果查询服务
@Service
@AllArgsConstructor
public class UserItemQueryService {

    private final DSLContext dsl;

    public PageResult<UserItemResult> findPage(UserPageQuery query) {
        // 分页查询
    }

    public List<UserItemResult> findAll(UserQuery query) {
        // 不分页查询
    }

    private Select<?> buildSelect() { }  // 列表专用字段映射
}

// 详情结果查询服务
@Service
@AllArgsConstructor
public class UserDetailQueryService {

    private final DSLContext dsl;

    public UserDetailResult findById(Long id) {
        // 单个查询
    }

    private Select<?> buildDetailSelect() { }  // 详情专用字段映射
}
```

## 原则

- **按结果拆分**：一种 DTO 对应一个 QueryService，避免不同查询逻辑混杂；
- 直接查数据库，绕过领域层；
- 查询不到抛 `BusinessException`；
- DTO 根据前端需求定制字段。