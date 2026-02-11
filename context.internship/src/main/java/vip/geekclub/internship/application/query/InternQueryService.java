package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.internship.Tables;
import org.jooq.generated.internship.tables.InternTable;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.BusinessException;

/**
 * 实习生查询服务
 */
@Service
@AllArgsConstructor
public class InternQueryService {

    private final DSLContext dslContext;
    private final InternTable internTable = Tables.Intern;

    /**
     * 根据认证ID获取实习生ID
     *
     * @param authId 用户认证ID
     * @return 实习生ID
     * @throws BusinessException 当实习生不存在时抛出404异常
     */
    public Long getInternIdByAuthId(String authId) {
        var record = dslContext
                .select(internTable.ID)
                .from(internTable)
                .where(internTable.AUTH_ID.eq(authId))
                .fetchOne();

        if (record == null) {
            throw new BusinessException(404, "当前用户不是实习生");
        }

        return record.get(internTable.ID);
    }
}
