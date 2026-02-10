package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.Thesis;

/**
 * 论文仓储接口
 * 负责论文模型的持久化操作
 */
@Repository
public interface ThesisRepository extends JpaRepository<@NonNull Thesis, @NonNull Long> {

    /**
     * 增加论文选择人数计数
     * 使用乐观锁机制，只有当当前人数小于最大人数时才更新成功
     *
     * @param thesisId 论文ID
     * @return 是否更新成功（false表示人数已达上限）
     */
    @Modifying
    @Query("UPDATE Thesis t SET t.currentSelections = t.currentSelections + 1 " +
            "WHERE t.id = :thesisId AND t.currentSelections < t.maxSelections")
    boolean incrementSelectionCount(@Param("thesisId") Long thesisId);
}
