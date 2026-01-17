package vip.geekclub.security.auth.query;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.CredentialTable;
import org.jooq.generated.tables.UserTable;
import org.springframework.stereotype.Service;
import vip.geekclub.security.auth.query.dto.CredentialResult;
import vip.geekclub.security.auth.common.AuthenticationType;
import vip.geekclub.security.auth.common.UserType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationQueryService {
    private final DSLContext query;
    private final CredentialTable credentialTable = Tables.Credential;
    private final UserTable userTable = Tables.User;

    public Optional<CredentialResult> getAuthenticationByIdentifier(String identifier, AuthenticationType type) {
        return query.select(
                        credentialTable.ID,
                        credentialTable.IDENTIFIER,
                        credentialTable.PASSWORD,
                        credentialTable.TYPE,
                        credentialTable.USER_ID,
                        userTable.USER_TYPE
                )
                .from(credentialTable)
                .join(userTable).on(credentialTable.USER_ID.eq(userTable.ID))
                .where(credentialTable.IDENTIFIER.eq(identifier))
                .and(credentialTable.TYPE.eq(type.toString()))
                .fetchOptional((record) ->
                        new CredentialResult(
                                record.get(credentialTable.ID),
                                record.get(credentialTable.IDENTIFIER),
                                record.get(credentialTable.PASSWORD),
                                AuthenticationType.valueOf(record.get(credentialTable.TYPE)),
                                record.get(credentialTable.USER_ID),
                                UserType.valueOf(record.get(userTable.USER_TYPE)))
                );
    }
}