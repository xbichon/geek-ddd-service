package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.Intern;

import java.util.List;
import java.util.Optional;

/**
 * 学生仓储接口
 * 负责学生模型的持久化操作
 */
@Repository
public interface InternRepository extends JpaRepository<@NonNull Intern, @NonNull Long> {

    /**
     * 根据用户ID查询实习生
     */
    Optional<Intern> findByAuthId(@NonNull String authId);

    /**
     * 根据ID列表查询所有实习生
     *
     * @param ids 实习生ID列表
     * @return 实习生列表
     */
    List<Intern> findAllByIdIn(@NonNull List<Long> ids);
}
