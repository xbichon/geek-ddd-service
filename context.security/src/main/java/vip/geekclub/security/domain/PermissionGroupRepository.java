package vip.geekclub.security.domain;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionGroupRepository extends JpaRepository<@NonNull PermissionGroup, @NonNull Long> {
    
    /**
     * 根据名称检查权限组是否存在
     *
     * @param name 权限组名称
     * @return 是否存在
     */
    @Query("SELECT COUNT(pg) > 0 FROM PermissionGroup pg WHERE pg.name = :name")
    boolean existsByName(@Param("name") String name);
}