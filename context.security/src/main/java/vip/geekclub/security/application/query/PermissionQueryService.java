package vip.geekclub.security.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import vip.geekclub.security.generated.Tables;
import vip.geekclub.security.generated.tables.PermissionTable;
import vip.geekclub.security.generated.tables.PrincipalRoleTable;
import vip.geekclub.security.generated.tables.PrincipalTable;
import vip.geekclub.security.generated.tables.RoleTable;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.BusinessException;

import java.util.Set;

@Service
@AllArgsConstructor
public class PermissionQueryService {

    private final DSLContext dslContext;
    private final PrincipalTable principalTable = Tables.Principal;
    private final PermissionTable permissionTable = Tables.Permission;
    private final PrincipalRoleTable principalRoleTable = Tables.PrincipalRole;
    private final RoleTable roleTable = Tables.Role;

    public Set<String> getPermissionByAuthId(String authId) {

        var userRecord = dslContext
                .select(principalTable.ID, principalTable.USER_TYPE)
                .from(principalTable)
                .where(principalTable.AUTH_ID.eq(authId))
                .fetchOne();
        if (userRecord == null) {
            throw new BusinessException(404, "用户不存在");
        }
        long principalId = userRecord.get(principalTable.ID);
        String userType = userRecord.get(principalTable.USER_TYPE);

        // 检查用户是否拥有系统管理员角色
        boolean hasSystemAdminRole = hasSystemAdminRole(principalId);

        if (hasSystemAdminRole) {
            return getAllPermissions(userType);
        }
        return getPermissionsById(principalId);
    }

    /**
     * 检查用户是否拥有系统管理员角色
     */
    private boolean hasSystemAdminRole(long principalId) {
        Integer count = dslContext
                .selectCount()
                .from(principalRoleTable)
                .join(roleTable).on(principalRoleTable.ROLE_ID.eq(roleTable.ID))
                .where(principalRoleTable.PRINCIPAL_ID.eq(principalId))
                .and(roleTable.IS_SYSTEM_ADMIN.eq((byte) 1))
                .fetchOneInto(int.class);
        return count != null && count > 0;
    }

    private Set<String> getPermissionsById(long principalId) {
        return dslContext
                .select(permissionTable.CODE)
                .from(principalRoleTable)
                .join(Tables.RolePermission).on(principalRoleTable.ROLE_ID.eq(Tables.RolePermission.ROLE_ID))
                .join(permissionTable).on(Tables.RolePermission.PERMISSION_ID.eq(permissionTable.ID))
                .where(principalRoleTable.PRINCIPAL_ID.eq(principalId))
                .fetchSet(record -> record.get(permissionTable.CODE));
    }

    private Set<String> getAllPermissions(String userType) {
        return dslContext
                .selectDistinct(permissionTable.CODE)
                .from(permissionTable)
                .where(permissionTable.USER_TYPE.eq(userType))
                .fetchSet(record -> record.get(permissionTable.CODE));
    }

}
