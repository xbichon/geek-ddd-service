package vip.geekclub.internship.adapter.controller.student;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.internship.application.query.InternQueryService;
import vip.geekclub.internship.application.query.dto.InternInfoResult;

import java.util.List;

/**
 * 实习生控制器
 */
@RestController("STUDENT_InternController")
@RequiredArgsConstructor
@RequestMapping("/student/internship/intern")
public class InternController {

    private final InternQueryService internQueryService;

    /**
     * 获取未选题学生列表
     */
    @GetMapping("/unselectedList")
    public ApiResponse<List<InternInfoResult>> unselectedList(UserPrincipal userPrincipal) {
        var currentInternId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        return ApiResponse.success(internQueryService.getUnselectedStudentsBySameAdvisor(currentInternId));
    }
}