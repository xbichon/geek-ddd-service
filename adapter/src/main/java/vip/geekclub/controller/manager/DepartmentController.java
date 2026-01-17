package vip.geekclub.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vip.geekclub.common.controller.ApiResponse;
import vip.geekclub.common.controller.WebCommandAdapter;
import vip.geekclub.common.exception.NotFoundException;
import vip.geekclub.config.security.UserSession;
import vip.geekclub.manager.command.dto.CreateDepartmentCommand;
import vip.geekclub.manager.command.dto.DeleteDepartmentCommand;
import vip.geekclub.manager.command.dto.UpdateDepartmentCommand;
import vip.geekclub.manager.query.DepartmentInfoQueryService;
import vip.geekclub.manager.query.DepartmentTreeQueryService;
import vip.geekclub.manager.query.dto.DepartmentInfoResult;
import vip.geekclub.manager.query.dto.DepartmentTreeResult;

import java.util.List;

/**
 * 部门管理控制器
 * 提供部门的增删改查操作
 */
@RestController
@RequestMapping("/manager/department")
@RequiredArgsConstructor
public class DepartmentController {

    private final WebCommandAdapter commandBus;
    private final DepartmentInfoQueryService departmentInfoQueryService;
    private final DepartmentTreeQueryService  departmentTreeQueryService;

    /**
     * 创建部门
     *
     * @param command 创建部门命令
     * @return 创建成功的部门ID
     */
    @PostMapping
    public ApiResponse<?> createDepartment(@RequestBody CreateDepartmentCommand command) {
        return commandBus.dispatchToWeb(command);
    }

    /**
     * 更新部门
     *
     * @param command 更新部门命令
     * @return 更新结果
     */
    @PutMapping
    public ApiResponse<?> updateDepartment(@RequestBody UpdateDepartmentCommand command) {
        return commandBus.dispatchToWeb(command);
    }

    /**
     * 删除部门
     *
     * @param id 部门ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteDepartment(@PathVariable Long id) {
        return commandBus.dispatchToWeb(new DeleteDepartmentCommand(id));
    }

    /**
     * 根据ID查询部门
     *
     * @param id 部门ID
     * @return 部门信息
     */
    @GetMapping("/{id}")
    public ApiResponse<DepartmentInfoResult> getDepartmentById(@PathVariable Long id, UserSession principal) {
        return departmentInfoQueryService.getDepartmentById(id)
                .map(ApiResponse::success)
                .orElseThrow(() -> new NotFoundException("部门不存在"));
    }

    /**
     * 获取启用部门的树形结构
     *
     * @return 部门树
     */
    @GetMapping("/tree/enabled")
    public ApiResponse<List<DepartmentTreeResult>> getEnabledDepartmentTree() {
        List<DepartmentTreeResult> tree = departmentTreeQueryService.getEnabledDepartmentTree();
        return ApiResponse.success(tree);
    }

    /**
     * 获取所有部门的树形结构
     *
     * @return 部门树
     */
    @GetMapping("/tree")
    public ApiResponse<List<DepartmentTreeResult>> getAllDepartmentTree() {
        List<DepartmentTreeResult> tree = departmentTreeQueryService.getAllDepartmentTree();
        return ApiResponse.success(tree);
    }
}