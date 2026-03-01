package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<@NonNull Role, @NonNull Long> {

    /**
     * 根据用户类型和系统管理员标识查询角色
     *
     * @param userType 用户类型
     * @param systemAdmin 是否是系统管理员角色
     * @return 角色
     */
    Optional<Role> findByUserTypeAndSystemAdmin(String userType, boolean systemAdmin);


    Integer countByUserTypeAndSystemAdminIsTrue(String userType);

    /**
     * 检查是否存在系统管理员角色
     *
     * @param userType 用户类型
     * @return 是否存在
     */
    boolean existsByUserTypeAndSystemAdminTrue(String userType);
}
