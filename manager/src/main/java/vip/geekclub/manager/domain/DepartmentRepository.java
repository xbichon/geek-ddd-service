package vip.geekclub.manager.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.manager.domain.Department;

/**
 * 部门仓库接口
 * 定义部门聚合的持久化操作
 *
 * @author leo
 * @since 1.0
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    /**
     * 检查是否存在指定名称和父部门ID的部门
     * 用于创建部门时的名称唯一性验证
     */
    boolean existsByNameAndParentId(String name, Long parentId);

    /**
     * 查询是否有子部门
     */
    Boolean existsByParentId(Long parentId);
}