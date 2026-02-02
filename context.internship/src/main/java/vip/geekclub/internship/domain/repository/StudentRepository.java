package vip.geekclub.internship.domain.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vip.geekclub.internship.domain.model.Student;

/**
 * 学生仓储接口
 * 负责学生模型的持久化操作
 */
@Repository
public interface StudentRepository extends JpaRepository<@NonNull Student, @NonNull Long> {

}
