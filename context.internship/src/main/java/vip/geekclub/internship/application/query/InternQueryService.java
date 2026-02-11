package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.InternTable;
import vip.geekclub.internship.generated.tables.SelectorTable;
import org.springframework.stereotype.Service;
import vip.geekclub.framework.exception.BusinessException;
import vip.geekclub.internship.application.query.dto.InternInfoResult;

import java.util.List;

/**
 * 实习生查询服务
 */
@Service
@AllArgsConstructor
public class InternQueryService {

    private final DSLContext dslContext;
    private final InternTable internTable = Tables.Intern;
    private final SelectorTable selectorTable = Tables.Selector;

    /**
     * 根据认证ID获取实习生ID
     *
     * @param authId 用户认证ID
     * @return 实习生ID
     * @throws BusinessException 当实习生不存在时抛出404异常
     */
    public Long getInternIdByAuthId(String authId) {
        var record = dslContext
                .select(internTable.ID)
                .from(internTable)
                .where(internTable.AUTH_ID.eq(authId))
                .fetchOne();

        if (record == null) {
            throw new BusinessException(404, "当前用户不是实习生");
        }

        return record.get(internTable.ID);
    }

    /**
     * 获取同指导老师且未选题的学生列表
     * <p>
     * 查询逻辑：
     * 1. 根据当前用户ID获取其指导老师
     * 2. 查询该指导老师下的所有学生
     * 3. 排除已在 selectorTable 中存在记录的学生（已选题的）
     *
     * @param currentInternId 当前用户实习生ID
     * @return 未选题的学生列表（包含ID和姓名）
     */
    public List<InternInfoResult> getUnselectedStudentsBySameAdvisor(Long currentInternId) {
        // 使用 NOT EXISTS 子查询，单查询实现
        List<InternInfoResult> students = dslContext
                .select(internTable.ID, internTable.NAME)
                .from(internTable)
                .where(internTable.ADVISOR_NAME.eq(
                        // 子查询：获取当前用户的指导老师
                        dslContext.select(internTable.ADVISOR_NAME)
                                .from(internTable)
                                .where(internTable.ID.eq(currentInternId))
                ))
                .andNotExists(
                        // 子查询：排除已选题的学生
                        dslContext.selectOne()
                                .from(selectorTable)
                                .where(selectorTable.STUDENT_ID.eq(internTable.ID))
                )
                .fetchInto(InternInfoResult.class);
        
        // 使用Stream直接查找当前用户实例
        InternInfoResult currentUser = students.stream()
                .filter(student -> student.id().equals(currentInternId))
                .findFirst()
                .orElse(null);
        
        // 如果找不到当前用户，说明该用户已经选择了论文
        if (currentUser == null) {
            throw new BusinessException(400, "当前用户已选择论文，不在未选题学生列表中");
        }
        
        // 将当前用户移到列表第一行
        students.remove(currentUser);
        students.addFirst(currentUser);
        
        return students;
    }


}
