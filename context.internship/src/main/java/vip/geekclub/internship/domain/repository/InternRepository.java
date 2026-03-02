package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.Intern;

import java.util.List;

/**
 * 学生仓储接口
 * 负责学生模型的持久化操作
 */
@Repository
public interface InternRepository extends CrudRepository<@NonNull Intern, @NonNull Long> {

    /**
     * 根据ID列表查询所有实习生
     *
     * @param ids 实习生ID列表
     * @return 实习生列表
     */
    List<Intern> findAllByIdIn(@NonNull List<Long> ids);

    /**
     * 查询所有未认证的实习生
     *
     * @return 未认证的实习生列表
     */
    List<Intern> findAllByAuthIdIsNull();
}
