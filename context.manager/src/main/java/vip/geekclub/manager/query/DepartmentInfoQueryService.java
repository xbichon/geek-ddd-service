package vip.geekclub.manager.query;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.RecordMapper;
import org.jooq.generated.tables.DepartmentTable;
import org.springframework.stereotype.Service;
import vip.geekclub.manager.query.dto.DepartmentInfoResult;

import java.util.Optional;

/**
 * 部门基础查询服务
 * 处理所有返回DepartmentInfoResult的查询
 *
 * @author geekclub
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class DepartmentInfoQueryService {

    private final DSLContext dslContext;
    private final static DepartmentTable table = DepartmentTable.Department;
    
    // 预定义字段数组，提高性能
    private static final Field<?>[] QUERY_FIELDS = {
            table.ID,
            table.NAME,
            table.SORT_ORDER,
            table.MANAGER,
            table.PARENT_ID,
            table.PHONE,
            table.STATUS,
            table.LEVEL,
            table.DESCRIPTION
    };
    
    // 使用 JOOQ 的 into() 方法，性能更好
    private static final RecordMapper<org.jooq.Record, DepartmentInfoResult> RESULT_MAPPER =
            record -> record.into(DepartmentInfoResult.class);

    /**
     * 根据ID查询部门
     */
    public Optional<DepartmentInfoResult> getDepartmentById(Long id) {
        return dslContext
                .select(QUERY_FIELDS)
                .from(table)
                .where(table.ID.eq(id))
                .fetchOptional(RESULT_MAPPER);
    }
}