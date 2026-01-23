package vip.geekclub.security.application.query;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.CredentialTable;
import org.jooq.generated.tables.PrincipalTable;
import org.springframework.stereotype.Service;
import vip.geekclub.security.application.query.dto.CredentialResult;
import vip.geekclub.security.domain.value.CredentialType;
import vip.geekclub.security.domain.value.UserType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationQueryService {
    private final DSLContext query;
    private final CredentialTable credentialTable = Tables.Credential;
    private final PrincipalTable principalTable = Tables.Principal;

    public Optional<CredentialResult> getAuthenticationByIdentifier(String identifier, CredentialType type) {
        return query.select(
                        credentialTable.ID,
                        credentialTable.IDENTIFIER,
                        credentialTable.PASSWORD,
                        credentialTable.TYPE,
                        credentialTable.USER_ID,
                        principalTable.APP_TYPE
                )
                .from(credentialTable)
                .join(principalTable).on(credentialTable.USER_ID.eq(principalTable.ID))
                .where(credentialTable.IDENTIFIER.eq(identifier))
                .and(credentialTable.TYPE.eq(type.toString()))
                .fetchOptional((record) ->
                        new CredentialResult(
                                record.get(credentialTable.ID),
                                record.get(credentialTable.IDENTIFIER),
                                record.get(credentialTable.PASSWORD),
                                CredentialType.valueOf(record.get(credentialTable.TYPE)),
                                record.get(credentialTable.USER_ID),
                                record.get(principalTable.APP_TYPE))
                );
    }
}