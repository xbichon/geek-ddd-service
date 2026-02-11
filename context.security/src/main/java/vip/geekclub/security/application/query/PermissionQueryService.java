package vip.geekclub.security.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.security.Tables;
import org.jooq.generated.security.tables.PermissionTable;
import org.jooq.generated.security.tables.PrincipalRoleTable;
import org.jooq.generated.security.tables.PrincipalTable;
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
        boolean isSuperAdmin = userRecord.get(principalTable.IS_SUPER_ADMIN)==1;
        //String userType = userRecord.get(principalTable.USER_TYPE);

        if (isSuperAdmin) {
            return getPermissions();
        }
        return getPermissionsById(principalId);
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

    private Set<String> getPermissions() {
        return dslContext
                .selectDistinct(permissionTable.CODE)
                .from(permissionTable)
                .fetchSet(record -> record.get(permissionTable.CODE));
    }

}
