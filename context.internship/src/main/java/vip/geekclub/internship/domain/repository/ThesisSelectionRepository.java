package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.ThesisSelection;

import java.util.List;

/**
 * 选题记录仓储接口
 * 负责选题记录模型的持久化操作
 */
@Repository
public interface ThesisSelectionRepository extends CrudRepository<@NonNull ThesisSelection, @NonNull Long> {

    /**
     * 统计已选过论文的学生数量
     *
     * @param studentIds 学生ID列表
     * @return 已选题的学生数量
     */
    long countBySelectorsStudentIdIn(List<Long> studentIds);

}