package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.Principal;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrincipalRepository extends JpaRepository<@NonNull Principal, @NonNull Long> {

    /**
     * 根据外部用户ID查询用户
     *
     * @param externalUuid 外部用户ID
     * @return 用户
     */
    Optional<Principal> findByAuthId(UUID externalUuid);

    /**
     * 检查是否存在超级管理员
     *
     * @return 是否存在超级管理员
     */
    boolean existsByIsSuperAdminTrue();
}