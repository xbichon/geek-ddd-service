package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.Thesis;

/**
 * 论文仓储接口
 * 负责论文模型的持久化操作
 */
@Repository
public interface ThesisRepository extends JpaRepository<@NonNull Thesis, @NonNull Long> {

}
