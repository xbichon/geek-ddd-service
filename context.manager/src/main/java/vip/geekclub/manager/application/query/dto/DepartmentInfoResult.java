package vip.geekclub.manager.application.query.dto;


import vip.geekclub.manager.domain.DepartmentStatus;

/**
 * 部门查询结果
 *
 * @author geekclub
 * @since 1.0
 */
public record DepartmentInfoResult(
        Long id,
        String name,
        Integer sortOrder,
        String manager,
        Long parentId,
        String phone,
        DepartmentStatus status,
        Integer level,
        String description
) {
}