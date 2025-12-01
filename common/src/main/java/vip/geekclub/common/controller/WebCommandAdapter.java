package vip.geekclub.common.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.common.command.Command;
import vip.geekclub.common.command.CommandBus;
import vip.geekclub.common.command.CommandResult;

@Component
@RequiredArgsConstructor
public class WebCommandAdapter {
    private final CommandBus commandBus;

    /**
     * 分发命令并返回Web API响应格式
     * <p>
     * 这是一个便捷方法，用于Web控制器中直接调用命令处理。
     * 它会自动将CommandResult包装成标准的ApiResponse格式。
     *
     * @param command 要分发的命令对象
     * @param <C>     命令的具体类型
     * @param <T>     返回结果中主键的类型
     * @return 包装后的API响应对象
     */
    public <C extends Command, T> ApiResponse<T> dispatchToWeb(C command) {
        CommandResult<T> result = commandBus.dispatch(command);
        return ApiResponse.success(result);
    }
}
