package vip.geekclub.security.domain.authentication.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.authentication.model.ThirdPartyCredential;
import vip.geekclub.security.domain.authentication.value.ThirdPartyType;

@Repository
public interface ThirdPartyCredentialRepository extends JpaRepository<@NonNull ThirdPartyCredential, @NonNull Long> {

    /**
     * 根据第三方提供商和标识符检查认证信息是否存在
     *
     * @param provider 第三方提供商
     * @param code 标识符
     * @return 是否存在
     */
    boolean existsByTypeAndCode(ThirdPartyType provider, String code);

    /**
     * 根据第三方提供商和用户ID检查认证信息是否存在
     *
     * @param provider 第三方提供商
     * @param principalId 用户ID
     * @return 是否存在
     */
    boolean existsByTypeAndPrincipalId(ThirdPartyType provider, Long principalId);
}