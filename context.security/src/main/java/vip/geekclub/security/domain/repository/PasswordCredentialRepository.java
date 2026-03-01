package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.Password;

import java.util.Optional;

@Repository
public interface PasswordCredentialRepository extends JpaRepository<@NonNull Password, @NonNull Long> {

    /**
     * 根据用户ID查询密码凭证
     *
     * @param principalId 用户ID
     * @return 密码凭证
     */
    Optional<Password> findByPrincipalId(Long principalId);

}