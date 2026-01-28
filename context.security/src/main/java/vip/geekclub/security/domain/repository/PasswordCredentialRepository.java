package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.PasswordCredential;
import vip.geekclub.security.domain.value.IdentifierType;

import java.util.Optional;

@Repository
public interface PasswordCredentialRepository extends JpaRepository<@NonNull PasswordCredential, @NonNull Long> {

    /**
     * 根据凭证类型和标识符值检查认证信息是否存在
     *
     * @param type 凭证类型
     * @param value 标识符值
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END " +
            "FROM PasswordCredential pc " +
            "JOIN pc.identifiers i " +
            "WHERE i.type = :type AND i.value = :value")
    boolean existsByIdentifierTypeAndValue(@Param("type") IdentifierType type,
                                        @Param("value") String value);


    Optional<PasswordCredential> findByIdentifiersValue( String value);

    /**
     * 根据凭证类型和用户 ID 检查是否已存在该类型的标识符
     *
     * @param type 凭证类型
     * @param principalId 用户 ID
     * @return 是否存在
     */
    @Query("SELECT CASE WHEN COUNT(pc) > 0 THEN true ELSE false END " +
            "FROM PasswordCredential pc " +
            "JOIN pc.identifiers i " +
            "WHERE pc.principalId = :principalId AND i.type = :type")
    boolean existsByIdentifierTypeAndPrincipalId(@Param("type") IdentifierType type,
                                             @Param("principalId") Long principalId);
}