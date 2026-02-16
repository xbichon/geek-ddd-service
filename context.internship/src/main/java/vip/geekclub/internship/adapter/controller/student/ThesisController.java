package vip.geekclub.internship.adapter.controller.student;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.internship.application.query.ThesisQueryService;
import vip.geekclub.internship.application.query.dto.ThesisItemResult;

import java.util.List;

/**
 * 论文管理控制器
 */
@RestController("STUDENT_ThesisController")
@RequiredArgsConstructor
@RequestMapping("/student/internship/thesis")
public class ThesisController {

    private final ThesisQueryService thesisQueryService;

    /**
     * 获取论文列表
     */
    @GetMapping("/list")
    public ApiResponse<List<ThesisItemResult>> list() {
        return ApiResponse.success(thesisQueryService.getThesisList());
    }
}