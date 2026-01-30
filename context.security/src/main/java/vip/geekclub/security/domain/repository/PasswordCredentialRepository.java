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
     * @return 是否存在
     */
    @Query("SELECT COUNT(*) =1 FROM Identifier i WHERE  i.value = :value AND i.userType = :userType")
    boolean existsByIdentifier(@Param("value") String value, @Param("userType") String userType);

    /**
     * 根据凭证类型和标识符值查询密码凭证
     *
     * @return 密码凭证
     */
    Optional<PasswordCredential> findByIdentifiersValueAndIdentifiersUserType(@Param("value") String value, @Param("userType") String userType);

}