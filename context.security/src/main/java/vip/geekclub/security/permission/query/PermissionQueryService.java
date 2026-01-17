package vip.geekclub.security.permission.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.PermissionTable;
import org.jooq.generated.tables.UserTable;
import org.springframework.stereotype.Service;
import vip.geekclub.security.auth.query.dto.UserResult;
import vip.geekclub.security.auth.common.UserType;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class PermissionQueryService {

    private final DSLContext dslContext;
    private final PermissionTable permissionTable = Tables.Permission;
    private final UserTable userTable = Tables.User;

    public Set<String> getPermissionByUserId(Long userId) {
        UserResult userResult = getUserById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (userResult.isAdmin()) {
            return getPermissions();
        } else {
            return getPermissionsByUserId(userId);
        }
    }
    
    private Optional<UserResult> getUserById(Long id) {
        var userResult = dslContext
                .select(userTable.fields())
                .from(userTable)
                .where(userTable.ID.eq(id))
                .fetchOptional();

        return userResult.map(record ->
                new UserResult(record.get(userTable.ID), UserType.valueOf(record.get(userTable.USER_TYPE)), record.get(userTable.IS_SUPER_ADMIN) == 1)
        );
    }
    
    private Set<String> getPermissionsByUserId(Long userId) {
        return dslContext
                .selectDistinct(permissionTable.CODE)
                .from(permissionTable)
                .join(Tables.RolePermission).on(permissionTable.ID.eq(Tables.RolePermission.PERMISSION_ID))
                .join(Tables.UserRole).on(Tables.RolePermission.ROLE_ID.eq(Tables.UserRole.ROLE_ID))
                .where(Tables.UserRole.USER_ID.eq(userId))
                .fetchSet(record -> record.get(permissionTable.CODE));
    }

    private Set<String> getPermissions() {
        return dslContext
                .selectDistinct(permissionTable.CODE)
                .from(permissionTable)
                .fetchSet(record -> record.get(permissionTable.CODE));
    }
}
