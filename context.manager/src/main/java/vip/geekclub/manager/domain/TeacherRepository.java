package vip.geekclub.manager.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 教师仓库接口
 * 定义教师聚合的持久化操作
 *
 * @author leo
 * @since 1.0
 */
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {

    /**
     * 检查是否存在指定手机号的教师
     * 用于创建教师时的手机号唯一性验证
     */
    boolean existsByPhone(String phone);

    /**
     * 检查是否存在指定邮箱的教师
     * 用于创建教师时的邮箱唯一性验证
     */
    boolean existsByEmail(String email);

    /**
     * 根据部门ID查询教师数量
     */
    long countByDepartmentId(Long departmentId);
}