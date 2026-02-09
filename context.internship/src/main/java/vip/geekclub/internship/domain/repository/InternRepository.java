package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.Intern;

/**
 * 学生仓储接口
 * 负责学生模型的持久化操作
 */
@Repository
public interface InternRepository extends JpaRepository<@NonNull Intern, @NonNull Long> {

}
