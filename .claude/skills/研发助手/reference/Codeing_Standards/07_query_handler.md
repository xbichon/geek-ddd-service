# 一、查询处理器编写规范

## 1、规范

- 文件放置在 `vip.geekclub.{业务领域}.application.query` 包下；
- 类名使用 `{业务}QueryService` 命名，如 `UserQueryService`；
- 使用 `@Service` 注解标记；
- 使用 Lombok 的 `@AllArgsConstructor` 注解进行依赖注入；
- 查询端采用 JOOQ 进行数据库查询，不经过领域层；
- 注入 `org.jooq.DSLContext` 用于构建查询；
- 返回类型根据需求使用 DTO 或基本类型，无需使用 `ApiResponse` 包装；
- 查询处理器只负责读取数据，不涉及写操作和事务管理。

## 2、示例

```java
@Service
@AllArgsConstructor
public class PermissionQueryService {

    private final DSLContext dslContext;
    private final PrincipalTable principalTable = Tables.Principal;
    private final PermissionTable permissionTable = Tables.Permission;
    private final PrincipalRoleTable principalRoleTable = Tables.PrincipalRole;

    /**
     * 根据用户认证ID获取权限列表
     *
     * @param authId 用户认证ID
     * @return 权限编码集合
     */
    public Set<String> getPermissionByAuthId(String authId) {
        var userRecord = dslContext
                .select(principalTable.ID, principalTable.USER_TYPE, principalTable.IS_SUPER_ADMIN)
                .from(principalTable)
                .where(principalTable.AUTH_ID.eq(authId))
                .fetchOne();

        if (userRecord == null) {
            throw new BusinessException(404, "用户不存在");
        }

        long principalId = userRecord.get(principalTable.ID);
        boolean isSuperAdmin = userRecord.get(principalTable.IS_SUPER_ADMIN) == 1;

        if (isSuperAdmin) {
            return getAllPermissions();
        }
        return getPermissionsByPrincipalId(principalId);
    }
}
```

## 3、编写原则

- **读写分离**：查询处理器直接操作数据库，绕过领域层以提高性能；
- **JOOQ 优先**：查询端使用 JOOQ 进行类型安全的 SQL 构建；
- **无事务**：查询操作不涉及数据修改，不需要 `@Transactional` 注解；
- **DTO 返回**：查询结果封装为 DTO，根据前端需求定制数据结构；
- **异常处理**：查询不到数据时抛出 `BusinessException`，由全局异常处理器统一处理。