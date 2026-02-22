# 查询处理器编写规范

## 1. 基础规范

- 文件放在 `vip.geekclub.{业务领域}.application.query` 包下；
- 类名：`{结果}QueryService`，如 `UserItemQueryService`；
- **一个查询结果对应一个服务**：列表用 `ItemQueryService`，详情用 `DetailQueryService`；
- 使用 `@Service` + `@AllArgsConstructor`；
- 注入 `DSLContext`，用 JOOQ 直接查数据库，不走领域层；
- 返回 DTO 或基本类型，不用 `ApiResponse`；
- **方法命名**：`findPage`（分页）、`findAll`（不分页）、`findById`（单个）；
- 查询不开启事务。

## 2. 性能规范

查询代码必须考虑 SQL 执行效率，避免慢查询。

### 2.1 避免 N+1 查询

- **禁止**在循环中执行单条查询
- **禁止**使用 correlated subquery（关联子查询，如 `EXISTS (SELECT ... WHERE id = outer.id)`）
- **优先**使用 JOIN 替代子查询

**示例**：
```java
// ❌ 错误：每行都执行子查询
.select(DSL.field(DSL.select(DSL.count(...)).from(...).where(...)))

// ✅ 正确：使用 LEFT JOIN 一次性获取
.leftJoin(selector).on(selector.STUDENT_ID.eq(intern.ID))
.select(selector.ID.isNotNull().as("selected"))
```

### 2.2 索引设计要求

涉及以下查询场景时，**必须**创建索引：

| 场景 | 索引类型 | 示例字段 |
|:---|:---|:---|
| WHERE 等值查询 | 单列索引 | `WHERE status = 'ACTIVE'` |
| WHERE 范围查询 | 单列索引 | `WHERE create_time > ?` |
| JOIN 关联条件 | 单列索引 | `JOIN ON a.fk_id = b.id` |
| ORDER BY | 索引 | `ORDER BY create_time DESC` |
| 多条件组合 | 复合索引 | `WHERE a = ? AND b = ?` |

**索引创建要求**：
- **必须**在数据迁移中添加索引（参考[数据迁移规范](../Migration/DataBase_migration.md)）
- 禁止仅在代码中依赖索引，不添加迁移脚本

### 2.3 执行计划检查

编写完查询后，**必须**使用 `EXPLAIN` 验证：

```sql
EXPLAIN SELECT ... FROM ...
-- 确认没有：Using filesort、Using temporary
-- 确认 type 为：ref/range/const，避免 ALL（全表扫描）
```

## 3. 代码示例

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

## 4. 设计原则

- **按结果拆分**：一种 DTO 对应一个 QueryService，避免不同查询逻辑混杂；
- 直接查数据库，绕过领域层；
- 查询不到抛 `BusinessException`；
- DTO 根据前端需求定制字段；
- **性能优先**：慢查询必须优化，必要时添加索引。