package vip.geekclub.internship.adapter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.contract.UserType;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.controller.WebCommandAdapter;
import vip.geekclub.framework.jooq.PageResult;
import vip.geekclub.framework.security.Authorize;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.internship.application.command.thesisselection.CreateThesisSelectionCommand;
import vip.geekclub.internship.application.query.*;
import vip.geekclub.internship.application.query.dto.*;

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
    private final ThesisSelectionListQueryService thesisSelectionListQueryService;
    private final AdvisorQueryService advisorQueryService;
    private final ClassNameQueryService classNameQueryService;
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
    @Authorize(userType = UserType.STUDENT)
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
    @Authorize(userType = UserType.STUDENT)
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
    @Authorize(userType = UserType.STUDENT)
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
    @Authorize(userType = UserType.STUDENT)
    public ApiResponse<List<InternInfoResult>> listUnselectedPeers(UserPrincipal userPrincipal) {
        var currentInternId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        List<InternInfoResult> students = internQueryService.getUnselectedStudentsBySameAdvisor(currentInternId);
        return ApiResponse.success(students);
    }

    /**
     * 获取论文选择结果列表
     *
     * @param query 查询参数（班级、指导老师、学生名字）
     * @return 论文选择结果列表
     */
    @GetMapping("/selectionList")
    @Authorize(userType = UserType.TEACHER)
    public ApiResponse<PageResult<ThesisSelectionListResult>> listThesisSelections(ThesisSelectionListQuery query) {
        PageResult<ThesisSelectionListResult> list = thesisSelectionListQueryService.getThesisSelectionList(query);
        return ApiResponse.success(list);
    }

    /**
     * 获取所有论文选择结果列表（不分页，无条件）
     *
     * @return 所有论文选择结果列表
     */
    @GetMapping("/allSelectionList")
    @Authorize(userType = UserType.TEACHER)
    public ApiResponse<List<ThesisSelectionListResult>> listAllThesisSelections() {
        List<ThesisSelectionListResult> list = thesisSelectionListQueryService.getAllThesisSelectionList();
        return ApiResponse.success(list);
    }

    /**
     * 获取所有指导老师姓名集合（去重）
     *
     * @return 指导老师姓名列表
     */
    @GetMapping("/advisorNames")
    @Authorize(userType = UserType.TEACHER)
    public ApiResponse<List<String>> getAllAdvisorNames() {
        List<String> advisorNames = advisorQueryService.getAllAdvisorNames();
        return ApiResponse.success(advisorNames);
    }

    /**
     * 获取所有班级名称集合（去重）
     *
     * @return 班级名称列表
     */
    @GetMapping("/classNames")
    @Authorize(userType = UserType.TEACHER)
    public ApiResponse<List<String>> getAllClassNames() {
        List<String> classNames = classNameQueryService.getAllClassNames();
        return ApiResponse.success(classNames);
    }
}