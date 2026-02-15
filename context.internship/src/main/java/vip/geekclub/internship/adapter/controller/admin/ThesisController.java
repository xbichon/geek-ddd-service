package vip.geekclub.internship.adapter.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.internship.application.query.*;
import vip.geekclub.internship.application.query.dto.*;

import java.util.List;

/**
 * 论文管理控制器
 * 提供论文及选题相关接口（JSON-RPC 风格命名）
 */
@Slf4j
@RestController("ADMIN_ThesisController")
@RequiredArgsConstructor
@RequestMapping("/admin/internship/thesis")
public class ThesisController {
    private final ThesisQueryService thesisQueryService;

    /**
     * 获取论文列表
     */
    @GetMapping("/list")
    public ApiResponse<List<ThesisListResult>> listThesis() {
        List<ThesisListResult> list = thesisQueryService.getThesisList();
        return ApiResponse.success(list);
    }
}