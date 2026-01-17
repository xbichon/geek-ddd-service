package vip.geekclub.security.auth.application.query;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.UserTable;
import org.springframework.stereotype.Service;
import vip.geekclub.security.auth.application.query.dto.UserResult;
import vip.geekclub.security.auth.domain.UserType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserQueryService {

    private final DSLContext dslContext;
    private final UserTable userTable = Tables.User;

    /**
     * 根据ID查询用户
     */
    public Optional<UserResult> getUserById(Long id) {
        var userResult = dslContext
                .select(userTable.fields())
                .from(userTable)
                .where(userTable.ID.eq(id))
                .fetchOptional();

        return userResult.map(record ->
                new UserResult(record.get(userTable.ID), UserType.valueOf(record.get(userTable.USER_TYPE)), record.get(userTable.IS_SUPER_ADMIN) == 1)
        );
    }
}