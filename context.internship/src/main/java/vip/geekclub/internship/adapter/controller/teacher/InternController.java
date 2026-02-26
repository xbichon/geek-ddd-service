package vip.geekclub.internship.adapter.controller.teacher;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.support.jooq.PageResult;
import vip.geekclub.internship.application.query.InternListQueryService;
import vip.geekclub.internship.application.query.dto.InternItemResult;
import vip.geekclub.internship.application.query.dto.InternPageQuery;

/**
 * 教师端实习生管理控制器
 */
@RestController("Teacher_InternController")
@RequestMapping("/teacher/internship/intern")
@RequiredArgsConstructor
public class InternController {

    private final InternListQueryService internListQueryService;

    /**
     * 分页查询实习生列表
     *
     * @param query 查询参数（班级、指导老师、是否选题）
     * @return 实习生列表（包含是否选题信息）
     */
    @GetMapping("/list")
    public ApiResponse<PageResult<InternItemResult>> list(InternPageQuery query) {
        return ApiResponse.success(internListQueryService.findPage(query));
    }
}
