package vip.geekclub.internship.adapter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.internship.application.query.ThesisQueryService;
import vip.geekclub.internship.application.query.dto.ThesisListResult;

import java.util.List;

/**
 * 论文控制器
 * 提供论文查询接口
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/internship/thesis")
public class ThesisController {

    private final ThesisQueryService thesisQueryService;

    /**
     * 获取论文列表
     * 返回论文及论文的成果集合
     *
     * @return 论文列表
     */
    @GetMapping
    public ApiResponse<List<ThesisListResult>> getThesisList() {
        List<ThesisListResult> list = thesisQueryService.getThesisList();
        return ApiResponse.success(list);
    }
}