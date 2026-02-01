package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.TeamApplication;

/**
 * 结组申请单仓储接口
 * 负责结组申请单模型的持久化操作
 */
@Repository
public interface TeamApplicationRepository extends CrudRepository<@NonNull TeamApplication, @NonNull Long> {

}