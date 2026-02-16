package vip.geekclub.internship.adapter.controller.student;

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
import vip.geekclub.internship.application.query.SelectionQueryService;
import vip.geekclub.internship.application.query.dto.SelectionDetailResult;

/**
 * 选题控制器
 */
@RestController("STUDENT_SelectionController")
@RequiredArgsConstructor
@RequestMapping("/student/internship/selection")
public class SelectionController {

    private final WebCommandAdapter commandBus;
    private final SelectionQueryService selectionQueryService;
    private final InternQueryService internQueryService;

    /**
     * 申请选题
     */
    @PostMapping("/apply")
    public ApiResponse<Void> apply(
            @Valid @RequestBody CreateThesisSelectionCommand command,
            UserPrincipal userPrincipal) {
        var internId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        command.setCreatorId(internId);
        return commandBus.dispatchToWeb(command);
    }

    /**
     * 获取选题详情
     */
    @GetMapping("/detail")
    public ApiResponse<SelectionDetailResult> detail(UserPrincipal userPrincipal) {
        var internId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        return ApiResponse.success(selectionQueryService.getDetail(internId));
    }

    /**
     * 检查选题状态
     */
    @GetMapping("/status")
    public ApiResponse<Boolean> status(UserPrincipal userPrincipal) {
        var internId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        return ApiResponse.success(selectionQueryService.hasSelected(internId));
    }
}