package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.Principal;

import java.util.Optional;

@Repository
public interface PrincipalRepository extends CrudRepository<@NonNull Principal, @NonNull Long> {

    /**
     * 根据外部用户ID查询用户
     *
     * @return 用户
     */
    Optional<Principal> findByAuthId(String authId);

    /**
     * 检查是否存在超级管理员
     *
     * @return 是否存在超级管理员
     */
    boolean existsByIsSuperAdminTrue();
}