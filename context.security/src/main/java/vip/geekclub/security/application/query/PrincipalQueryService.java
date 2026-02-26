package vip.geekclub.security.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.NotFoundException;
import vip.geekclub.security.generated.Tables;
import vip.geekclub.security.generated.tables.PrincipalTable;

@Service
@AllArgsConstructor
public class PrincipalQueryService {

    private final DSLContext dslContext;
    private final PrincipalTable principalTable = Tables.Principal;

    /**
     * 根据 authId 查询用户类型
     *
     * @param authId 认证标识
     * @return 用户类型
     */
    public String getUserTypeByAuthId(String authId) {
        var record = dslContext
                .select(principalTable.USER_TYPE)
                .from(principalTable)
                .where(principalTable.AUTH_ID.eq(authId))
                .fetchOne();

        if (record == null) {
            throw new NotFoundException("用户不存在");
        }

        return record.get(principalTable.USER_TYPE);
    }
}
