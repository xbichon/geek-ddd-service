package vip.geekclub.security.domain.authorization.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.authorization.model.Role;

@Repository
public interface RoleRepository extends CrudRepository<@NonNull Role, @NonNull Long> {

    Integer countByUserTypeAndSystemAdminIsTrue(String userType);

}
