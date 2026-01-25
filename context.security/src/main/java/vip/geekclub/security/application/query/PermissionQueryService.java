package vip.geekclub.security.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.PermissionTable;
import org.jooq.generated.tables.PrincipalRoleTable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
public class PermissionQueryService {

    private final DSLContext dslContext;
    private final PermissionTable permissionTable = Tables.Permission;
    private final PrincipalRoleTable principalRoleTable = Tables.PrincipalRole;

    public Set<String> getPermissionByUserId(Long userId) {
        var roleIds = getRoleByPrincipalId(userId);

        if (roleIds.contains(-1L)) {
            return getPermissions();
        }

        return getPermissionsByRoleIds(roleIds);
    }

    private Set<Long> getRoleByPrincipalId(Long id) {
        return dslContext
                .select(principalRoleTable.ROLE_ID)
                .from(principalRoleTable)
                .where(principalRoleTable.PRINCIPAL_ID.eq(id))
                .fetchSet(record -> record.get(principalRoleTable.ROLE_ID));
    }

    private Set<String> getPermissionsByRoleIds(Set<Long> roleIds) {
        if (roleIds.isEmpty()) {
            return Set.of();
        }

        return dslContext
                .selectDistinct(permissionTable.CODE)
                .from(permissionTable)
                .where(Tables.PrincipalRole.PRINCIPAL_ID.in(roleIds))
                .fetchSet(record -> record.get(permissionTable.CODE));
    }

    private Set<String> getPermissions() {
        return dslContext
                .selectDistinct(permissionTable.CODE)
                .from(permissionTable)
                .fetchSet(record -> record.get(permissionTable.CODE));
    }
}
