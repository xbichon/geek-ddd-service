package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.Principal;

@Repository
public interface PrincipalRepository extends JpaRepository<@NonNull Principal,@NonNull Long> {
     boolean existsByUserType(String userType);
}