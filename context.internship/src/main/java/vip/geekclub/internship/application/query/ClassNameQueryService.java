package vip.geekclub.internship.application.query;

import lombok.AllArgsConstructor;
import org.jooq.DSLContext;
import vip.geekclub.internship.generated.Tables;
import vip.geekclub.internship.generated.tables.InternTable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 班级名称查询服务
 * 专门处理班级相关信息的查询
 */
@Service
@AllArgsConstructor
public class ClassNameQueryService {

    private final DSLContext dslContext;
    private final InternTable internTable = Tables.Intern;

    /**
     * 获取所有班级名称集合（去重）
     *
     * @return 班级名称列表
     */
    public List<String> getAllClassNames() {
        return dslContext
                .selectDistinct(internTable.CLASS_NAME)
                .from(internTable)
                .where(internTable.CLASS_NAME.isNotNull())
                .orderBy(internTable.CLASS_NAME)
                .fetch(internTable.CLASS_NAME);
    }
}