package vip.geekclub.manager.query.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import vip.geekclub.manager.domain.DepartmentStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * 部门树形结构结果
 *
 * @author geekclub
 * @since 1.0
 */
@Getter @Accessors(fluent = true)
@RequiredArgsConstructor
public class DepartmentTreeResult {
    
    private final Long id;
    private final String name;
    private final Integer sortOrder;
    private final Long parentId;
    private final DepartmentStatus status;
    private final Integer level;
    private final List<DepartmentTreeResult> children =new ArrayList<>();

    /**
     * 添加子部门
     *
     * @param child 子部门
     */
    public void addChild(DepartmentTreeResult child) {
        this.children.add(child);
    }

    public boolean hasParent() {
        return parentId != null;
    }

    public boolean isRoot() {
        return parentId == null;
    }
}