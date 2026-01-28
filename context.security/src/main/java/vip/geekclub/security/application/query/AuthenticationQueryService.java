package vip.geekclub.security.application.query;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.generated.Tables;
import org.jooq.generated.tables.IdentifierTable;
import org.jooq.generated.tables.PasswordCredentialTable;
import org.jooq.generated.tables.PrincipalTable;
import org.springframework.stereotype.Service;
import vip.geekclub.security.application.query.dto.CredentialResult;
import vip.geekclub.security.domain.value.CredentialType;
import vip.geekclub.security.domain.value.IdentifierType;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationQueryService {
    private final DSLContext query;
    private final PasswordCredentialTable passwordCredentialTable = Tables.PasswordCredential;
    private final IdentifierTable identifierTable = Tables.Identifier;
    private final PrincipalTable principalTable = Tables.Principal;

    public Optional<CredentialResult> getAuthenticationByIdentifier(String identifier, IdentifierType type) {
        return query.select(
                        passwordCredentialTable.ID,
                        identifierTable.VALUE,
                        passwordCredentialTable.PASSWORD,
                        identifierTable.TYPE,
                        passwordCredentialTable.PRINCIPAL_ID,
                        principalTable.USER_TYPE
                )
                .from(passwordCredentialTable)
                .join(identifierTable).on(identifierTable.PASSWORD_CREDENTIAL_ID.eq(passwordCredentialTable.ID))
                .join(principalTable).on(passwordCredentialTable.PRINCIPAL_ID.eq(principalTable.ID))
                .where(identifierTable.VALUE.eq(identifier))
                .and(identifierTable.TYPE.eq(type.toString()))
                .fetchOptional((record) ->
                        new CredentialResult(
                                record.get(passwordCredentialTable.ID),
                                record.get(identifierTable.VALUE),
                                record.get(passwordCredentialTable.PASSWORD),
                                CredentialType.valueOf(record.get(identifierTable.TYPE)),
                                record.get(passwordCredentialTable.PRINCIPAL_ID),
                                record.get(principalTable.USER_TYPE))
                );
    }
}