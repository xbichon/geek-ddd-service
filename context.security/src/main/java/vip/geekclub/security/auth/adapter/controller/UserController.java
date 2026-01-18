package vip.geekclub.security.auth.adapter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.controller.WebCommandAdapter;
import vip.geekclub.security.auth.application.command.dto.CreateUserCommand;
import vip.geekclub.security.auth.application.command.dto.DeleteUserCommand;

/**
 * 用户管理控制器
 * 提供用户的增删改查操作
 */
@RestController
@RequestMapping("/manager/user")
@RequiredArgsConstructor
public class UserController {

    private final WebCommandAdapter commandBus;

    /**
     * 创建用户
     * 
     * @param command 创建用户命令
     * @return 创建成功的用户ID
     */
    @PostMapping
    public ApiResponse<?> createUser(@RequestBody CreateUserCommand command) {
        return commandBus.dispatchToWeb(command);
    }

    /**
     * 删除用户
     * 
     * @param id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteUser(@PathVariable Long id) {
        return commandBus.dispatchToWeb(new DeleteUserCommand(id));
    }
}