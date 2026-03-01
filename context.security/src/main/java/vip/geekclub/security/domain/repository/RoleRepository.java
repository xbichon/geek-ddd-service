package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.Role;

import java.util.Optional;

@Repository
public interface RoleRepository extends CrudRepository<@NonNull Role, @NonNull Long> {

    Integer countByUserTypeAndSystemAdminIsTrue(String userType);

}
