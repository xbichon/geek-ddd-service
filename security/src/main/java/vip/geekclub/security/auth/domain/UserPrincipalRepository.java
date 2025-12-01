package vip.geekclub.security.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrincipalRepository extends JpaRepository<UserPrincipal, Long> {
     boolean existsBy();
}