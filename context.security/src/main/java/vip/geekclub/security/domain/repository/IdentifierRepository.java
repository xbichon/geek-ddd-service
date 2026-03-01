package vip.geekclub.security.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.security.domain.model.Identifier;

import java.util.Optional;

@Repository
public interface IdentifierRepository extends JpaRepository<@NonNull Identifier, @NonNull Long> {

    /**
     * 根据标识符值和用户类型检查是否存在
     *
     * @param value    标识符值
     * @param userType 用户类型
     * @return 是否存在
     */
    boolean existsByValueAndUserType(String value, String userType);

    /**
     * 根据标识符值和用户类型查询标识符
     *
     * @param value    标识符值
     * @param userType 用户类型
     * @return 标识符
     */
    Optional<Identifier> findByValueAndUserType(String value, String userType);
}
