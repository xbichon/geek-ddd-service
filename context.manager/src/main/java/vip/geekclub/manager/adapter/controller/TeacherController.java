package vip.geekclub.manager.adapter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vip.geekclub.contract.UserType;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.controller.WebCommandAdapter;
import vip.geekclub.framework.security.Authorize;
import vip.geekclub.manager.application.command.dto.CreateTeacherCommand;
import vip.geekclub.manager.application.command.dto.DeleteTeacherCommand;
import vip.geekclub.manager.application.command.dto.UpdateTeacherCommand;

/**
 * 教师管理控制器
 * 提供教师的增删改查操作
 */
@RestController
@RequestMapping("/teacher/manager/teacher")
@RequiredArgsConstructor
@Authorize(userType = UserType.TEACHER)
public class TeacherController {

    private final WebCommandAdapter commandBus;

    /**
     * 创建教师
     *
     * @param command 创建教师命令
     * @return 创建成功的教师ID
     */
    @PostMapping
    public ApiResponse<Long> createTeacher(@RequestBody CreateTeacherCommand command) {
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
}