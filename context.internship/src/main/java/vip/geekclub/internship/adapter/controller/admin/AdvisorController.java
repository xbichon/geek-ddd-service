package vip.geekclub.internship.adapter.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.internship.application.query.AdvisorQueryService;

import java.util.List;

@RestController
@RequestMapping("/admin/internship/advisor")
@RequiredArgsConstructor
public class AdvisorController {
    private final AdvisorQueryService advisorQueryService;

    /**
     * 获取所有指导老师姓名集合（去重）
     *
     * @return 指导老师姓名列表
     */
    @GetMapping("/list")
    public ApiResponse<List<String>> getAllAdvisorNames() {
        List<String> advisorNames = advisorQueryService.getAllAdvisorNames();
        return ApiResponse.success(advisorNames);
    }
}
