package vip.geekclub.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vip.geekclub.common.command.IdResult;
import vip.geekclub.common.controller.ApiResponse;
import vip.geekclub.common.controller.WebCommandAdapter;
import vip.geekclub.config.security.UserSession;
import vip.geekclub.manager.command.dto.CreateTeacherCommand;
import vip.geekclub.manager.command.dto.DeleteTeacherCommand;
import vip.geekclub.manager.command.dto.UpdateTeacherCommand;

/**
 * 教师管理控制器
 * 提供教师的增删改查操作
 */
@RestController
@RequestMapping("/manager/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final WebCommandAdapter commandBus;

    /**
     * 创建教师
     *
     * @param command 创建教师命令
     * @return 创建成功的教师ID
     */
    @PostMapping
    public ApiResponse<IdResult> createTeacher(@RequestBody CreateTeacherCommand command) {
        return commandBus.dispatchToWeb(command);
    }

    /**
     * 更新教师信息
     *
     * @param command 更新教师命令
     * @return 更新结果
     */
    @PutMapping
    public ApiResponse<Void> updateTeacher(@RequestBody UpdateTeacherCommand command) {
        return commandBus.dispatchToWeb(command);
    }

    /**
     * 删除教师
     *
     * @param id 教师ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteTeacher(@PathVariable Long id) {
        return commandBus.dispatchToWeb(new DeleteTeacherCommand(id));
    }

    /**
     * 根据ID查询教师
     *
     * @param id 教师ID
     * @return 教师信息
     */
    @GetMapping("/{id}")
    public ApiResponse<Object> getTeacherById(@PathVariable Long id, UserSession principal) {
        // TODO: 实现查询单个教师功能 - 需要创建TeacherInfoQueryService
        throw new UnsupportedOperationException("查询单个教师功能待实现");
    }

    /**
     * 根据部门ID查询教师列表
     *
     * @param departmentId 部门ID
     * @return 教师列表
     */
    @GetMapping("/department/{departmentId}")
    public ApiResponse<Object> getTeachersByDepartment(@PathVariable Long departmentId) {
        // TODO: 实现根据部门查询教师列表功能 - 需要创建TeacherQueryService
        throw new UnsupportedOperationException("根据部门查询教师列表功能待实现");
    }
}