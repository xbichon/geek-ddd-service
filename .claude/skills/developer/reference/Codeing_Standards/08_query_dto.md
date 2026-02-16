# 一、查询数据传输对象（Query DTO）编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.application.query.dto` 包下；
- 类名使用 `{业务}Result` 或 `{业务}Query` 命名：
  - 查询结果：`DepartmentInfoResult`、`UserListResult`
  - 查询参数：`UserListQuery`、`OrderPageQuery`
  - **列表单项**：返回列表时，单项命名用 `{实体}ItemResult`，如 `ThesisItemResult`
- 使用 Java Record 定义，确保不可变性；
- 针对读操作进行优化设计，只包含查询所需字段；
- 可使用 JOOQ 生成的枚举类型作为字段类型。

## 2、示例

### 查询结果 DTO

```java
/**
 * 部门查询结果
 */
public record DepartmentInfoResult(
        Long id,
        String name,
        Integer sortOrder,
        String manager,
        Long parentId,
        String phone,
        DepartmentStatus status,
        Integer level,
        String description
) {
}
```

### 查询参数 DTO

```java
public record DepartmentTreeQuery(
        Long parentId,
        Boolean includeDisabled
) {
    public DepartmentTreeQuery {
        if (parentId == null) {
            parentId = 0L;
        }
        if (includeDisabled == null) {
            includeDisabled = false;
        }
    }
}
```

## 3、设计原则

- **面向查询**：Query DTO 专门为查询场景设计，字段根据前端展示需求确定；
- **扁平化结构**：查询结果通常为扁平结构，避免嵌套对象，便于直接使用；
- **不可变性**：使用 Record 确保查询结果不可被修改；
- **默认值**：在 compact constructor 中为查询参数设置合理的默认值；
- **与领域模型解耦**：Query DTO 不依赖领域模型，可以聚合多个领域的数据。