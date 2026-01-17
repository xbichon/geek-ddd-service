package vip.geekclub.security.permission.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    /**
     * 根据权限码查询权限是否存在
     *
     * @param code 权限码
     * @return 是否存在
     */
    @Query("SELECT COUNT(p) > 0 FROM Permission p WHERE p.permissionCode.code = :code")
    Boolean existsByCode(@Param("code") String code);

    /**
     * 根据权限组ID查询是否存在权限
     *
     * @param permissionGroupId 权限组ID
     * @return 是否存在
     */
    Boolean existsByPermissionGroupId(Long permissionGroupId);
}