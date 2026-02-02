package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.ThesisSelection;

/**
 * 选题记录仓储接口
 * 负责选题记录模型的持久化操作
 */
@Repository
public interface ThesisSelectionRepository extends CrudRepository<@NonNull ThesisSelection, @NonNull Long> {

}