package vip.geekclub.framework.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vip.geekclub.framework.command.Command;
import vip.geekclub.framework.command.CommandBus;

@Component
@RequiredArgsConstructor
public class WebCommandAdapter {

    private final CommandBus commandBus;

    /**
     * 分发命令并返回Web API响应格式
     * <p>
     * 这是一个便捷方法，用于Web控制器中直接调用命令处理。
     * 它会自动将命令执行结果包装成标准的ApiResponse格式。
     *
     * @param command 要分发的命令对象
     * @param <R>     返回结果类型
     * @return 包装后的API响应对象
     */
    public <R> ApiResponse<R> dispatchToWeb(Command<R> command) {
        R result = commandBus.dispatch(command);
        return ApiResponse.success(result);
    }
}
