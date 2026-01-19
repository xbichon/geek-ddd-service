package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.value.CredentialType;
import vip.geekclub.security.domain.model.Credential;

@Repository
public interface CredentialRepository extends JpaRepository<@NonNull Credential,@NonNull Long> {

    /**
     * 根据认证类型和标识符检查认证信息是否存在
     *
     * @param type 认证类型
     * @param identifier 标识符
     * @return 是否存在
     */
//    @Query("SELECT COUNT(ac) > 0 FROM Credential ac WHERE ac.type = :type AND ac.identifier = :identifier")
    boolean existsByTypeAndIdentifier(CredentialType type, String identifier);

    /**
     * 根据认证类型和用户ID检查认证信息是否存在
     *
     * @param type 认证类型
     * @param principalId 用户ID
     * @return 是否存在
     */
    boolean existsByTypeAndPrincipalId(CredentialType type, Long principalId);
}