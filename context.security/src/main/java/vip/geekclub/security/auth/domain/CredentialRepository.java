package vip.geekclub.security.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CredentialRepository extends JpaRepository<Credential, Long> {

    /**
     * 根据认证类型和标识符检查认证信息是否存在
     *
     * @param type 认证类型
     * @param identifier 标识符
     * @return 是否存在
     */
    @Query("SELECT COUNT(ac) > 0 FROM Credential ac WHERE ac.type = :type AND ac.identifier = :identifier")
    boolean existsByTypeAndIdentifier(@Param("type") AuthenticationType type, @Param("identifier") String identifier);
}