package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.InternTable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 指导老师查询服务
 * 专门处理指导老师相关信息的查询
 */
@Service
@AllArgsConstructor
public class AdvisorQueryService {

    private final DSLContext dslContext;
    private final InternTable internTable = Tables.Intern;

    /**
     * 获取所有指导老师姓名集合（去重）
     *
     * @return 指导老师姓名列表
     */
    public List<String> getAllAdvisorNames() {
        return dslContext
                .selectDistinct(internTable.ADVISOR_NAME)
                .from(internTable)
                .where(internTable.ADVISOR_NAME.isNotNull())
                .orderBy(internTable.ADVISOR_NAME)
                .fetch(internTable.ADVISOR_NAME);
    }
}