package vip.geekclub.security.domain;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrincipalRepository extends JpaRepository<@NonNull Principal,@NonNull Long> {
     boolean existsBy();
}