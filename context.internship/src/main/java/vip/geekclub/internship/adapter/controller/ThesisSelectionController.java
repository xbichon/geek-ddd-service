package vip.geekclub.internship.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.command.CommandDispatcher;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.controller.WebCommandAdapter;
import vip.geekclub.internship.application.command.thesisselection.CreateThesisSelectionCommand;

/**
 * 论文选题控制器
 * 提供选题申请接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/internship/thesis-selection")
public class ThesisSelectionController {
    private final WebCommandAdapter commandBus;

    /**
     * 创建选题申请
     *
     * @param command 选题命令
     * @return 操作结果
     */
    @PostMapping
    public ApiResponse<Void> createThesisSelection(@Valid @RequestBody CreateThesisSelectionCommand command) {
        return commandBus.dispatchToWeb(command);
    }
}