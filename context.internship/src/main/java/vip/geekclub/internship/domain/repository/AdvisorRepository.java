package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.Advisor;

/**
 * 指导教师仓储接口
 * 负责指导教师模型的持久化操作
 */
@Repository
public interface AdvisorRepository extends JpaRepository<@NonNull Advisor, @NonNull Long> {

}