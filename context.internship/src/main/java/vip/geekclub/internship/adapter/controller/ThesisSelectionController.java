package vip.geekclub.internship.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.command.CommandContext;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.controller.WebCommandAdapter;
import vip.geekclub.internship.application.command.thesisselection.CreateThesisSelectionCommand;
import vip.geekclub.internship.application.query.InternQueryService;
import vip.geekclub.internship.application.query.ThesisSelectionQueryService;
import vip.geekclub.internship.application.query.dto.ThesisSelectionDetailResult;

/**
 * 论文选题控制器
 * 提供选题申请和查询接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/internship/thesis-selection")
public class ThesisSelectionController {

    private final WebCommandAdapter commandBus;
    private final ThesisSelectionQueryService queryService;
    private final InternQueryService internQueryService;

    /**
     * 创建选题申请
     *
     * @param command 选题命令（creatorId 由后端从上下文获取并设置）
     * @return 操作结果
     */
    @PostMapping
    public ApiResponse<Void> createThesisSelection(@Valid @RequestBody CreateThesisSelectionCommand command) {
        // 从上下文获取当前用户，查询实习生ID，并设置到命令中
        var principal = CommandContext.getCurrentPrincipal();
        var currentId = internQueryService.getInternIdByAuthId(principal.authId());

        // 使用 withCreatorId 设置创建者ID（无视前端传入的值）
        command.setCreatorId(currentId);

        return commandBus.dispatchToWeb(command);
    }

    /**
     * 获取当前用户的选题详情
     *
     * @return 选题详情
     */
    @GetMapping("/detail")
    public ApiResponse<ThesisSelectionDetailResult> getSelectionDetail() {
        // 从上下文获取当前用户，查询实习生ID
        var principal = CommandContext.getCurrentPrincipal();
        var internId = internQueryService.getInternIdByAuthId(principal.authId());

        ThesisSelectionDetailResult result = queryService.getCurrentUserSelectionDetail(internId);
        return ApiResponse.success(result);
    }
}