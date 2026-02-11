package vip.geekclub.internship.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.controller.WebCommandAdapter;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.internship.application.command.thesisselection.CreateThesisSelectionCommand;
import vip.geekclub.internship.application.query.InternQueryService;
import vip.geekclub.internship.application.query.ThesisQueryService;
import vip.geekclub.internship.application.query.ThesisSelectionQueryService;
import vip.geekclub.internship.application.query.dto.InternInfoResult;
import vip.geekclub.internship.application.query.dto.ThesisListResult;
import vip.geekclub.internship.application.query.dto.ThesisSelectionDetailResult;

import java.util.List;

/**
 * 论文管理控制器
 * 提供论文及选题相关接口（JSON-RPC 风格命名）
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/internship/thesis")
public class ThesisController {

    private final WebCommandAdapter commandBus;
    private final ThesisQueryService thesisQueryService;
    private final ThesisSelectionQueryService thesisSelectionQueryService;
    private final InternQueryService internQueryService;

    // ==================== 论文相关 ====================

    /**
     * 获取论文列表
     */
    @GetMapping("/list")
    public ApiResponse<List<ThesisListResult>> listThesis() {
        List<ThesisListResult> list = thesisQueryService.getThesisList();
        return ApiResponse.success(list);
    }

    // ==================== 选题相关 ====================

    /**
     * 申请选题
     *
     * @param command 选题命令
     * @param userPrincipal 当前用户
     * @return 操作结果
     */
    @PostMapping("/applySelection")
    public ApiResponse<Void> applySelection(
            @Valid @RequestBody CreateThesisSelectionCommand command,
            UserPrincipal userPrincipal) {
        var internId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        command.setCreatorId(internId);
        return commandBus.dispatchToWeb(command);
    }

    /**
     * 获取当前用户的选题详情
     *
     * @param userPrincipal 当前用户
     * @return 选题详情
     */
    @GetMapping("/getSelectionDetail")
    public ApiResponse<ThesisSelectionDetailResult> getSelectionDetail(UserPrincipal userPrincipal) {
        var internId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        ThesisSelectionDetailResult result = thesisSelectionQueryService.getCurrentUserSelectionDetail(internId);
        return ApiResponse.success(result);
    }

    /**
     * 检查当前用户是否已选题
     *
     * @param userPrincipal 当前用户
     * @return true-已选题，false-未选题
     */
    @GetMapping("/checkSelectionStatus")
    public ApiResponse<Boolean> checkSelectionStatus(UserPrincipal userPrincipal) {
        var internId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        boolean hasSelected = thesisSelectionQueryService.hasCurrentUserSelected(internId);
        return ApiResponse.success(hasSelected);
    }

    /**
     * 获取同指导老师且未选题的学生列表
     *
     * @param userPrincipal 当前用户
     * @return 未选题的学生列表
     */
    @GetMapping("/unselectedStudent")
    public ApiResponse<List<InternInfoResult>> listUnselectedPeers(UserPrincipal userPrincipal) {
        var currentInternId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        List<InternInfoResult> students = internQueryService.getUnselectedStudentsBySameAdvisor(currentInternId);
        return ApiResponse.success(students);
    }
}