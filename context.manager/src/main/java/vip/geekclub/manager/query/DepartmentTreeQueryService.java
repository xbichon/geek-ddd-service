package vip.geekclub.manager.query;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.RecordMapper;
import org.jooq.generated.tables.DepartmentTable;
import org.springframework.stereotype.Service;
import vip.geekclub.manager.common.DepartmentStatus;
import vip.geekclub.manager.query.dto.DepartmentTreeResult;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 部门树形结构查询服务
 * 处理所有返回DepartmentTreeResult的查询
 *
 * @author geekclub
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
public class DepartmentTreeQueryService {

    private final DSLContext dslContext;
    private static final DepartmentTable table = DepartmentTable.Department;
    private static final Field<?>[] QUERY_FIELDS = {
            table.ID,
            table.NAME,
            table.SORT_ORDER,
            table.PARENT_ID,
            table.STATUS,
            table.LEVEL
    };
    private static final RecordMapper<org.jooq.Record, DepartmentTreeResult> RESULT_MAPPER = (record) -> record.into(DepartmentTreeResult.class);

    /**
     * 查询所有部门树结构（包含所有状态的部门）
     */
    public List<DepartmentTreeResult> getAllDepartmentTree() {
        List<DepartmentTreeResult> departmentTreeResultList = dslContext
                .select(QUERY_FIELDS)
                .from(table)
                .orderBy(table.SORT_ORDER.asc(), table.ID.asc())
                .fetch(RESULT_MAPPER);
        return buildDepartmentTree(departmentTreeResultList);
    }

    /**
     * 查询启用状态的部门树结构
     */
    public List<DepartmentTreeResult> getEnabledDepartmentTree() {
        List<DepartmentTreeResult> departmentTreeResultList = dslContext
                .select(QUERY_FIELDS)
                .from(table)
                .where(table.STATUS.eq(DepartmentStatus.ENABLED.toString()))
                .orderBy(table.SORT_ORDER.asc(), table.ID.asc())
                .fetch(RESULT_MAPPER);
        return buildDepartmentTree(departmentTreeResultList);
    }

    /**
     * 构建部门树结构
     */
    private List<DepartmentTreeResult> buildDepartmentTree(List<DepartmentTreeResult> departments) {
        if (departments.isEmpty()) {
            return departments;
        }

        // 1. 转换为DepartmentTreeResult并建立ID映射
        Map<Long, DepartmentTreeResult> departmentMap = departments.stream()
                .collect(Collectors.toMap(DepartmentTreeResult::id, dept -> dept));

        // 2. 建立父子关系
        departments.stream()
                .filter(DepartmentTreeResult::hasParent)
                .forEach(dept -> {
                    DepartmentTreeResult parent = departmentMap.get(dept.parentId());
                    if (parent == null) return;

                    parent.addChild(dept);
                });

        // 3. 找到根节点并返回，按排序号排序
        return departments.stream()
                .filter(DepartmentTreeResult::isRoot)
                .toList();
    }
}