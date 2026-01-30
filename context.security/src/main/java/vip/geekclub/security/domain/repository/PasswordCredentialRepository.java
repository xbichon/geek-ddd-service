package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.PasswordCredential;

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
    boolean existsByIdentifierValueAndIdentifierType(String type, String value);



    Optional<PasswordCredential> findByIdentifierValue( String value);

    Optional<PasswordCredential> find(Long principalId);

}