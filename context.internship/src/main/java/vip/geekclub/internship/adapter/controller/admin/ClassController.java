package vip.geekclub.internship.adapter.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.internship.application.query.ClassNameQueryService;

import java.util.List;

@RestController
@RequestMapping("admin/internship/class")
@RequiredArgsConstructor
public class ClassController {
    private final ClassNameQueryService classNameQueryService;


    /**
     * 获取所有班级名称集合（去重）
     *
     * @return 班级名称列表
     */
    @GetMapping("/list")
    public ApiResponse<List<String>> getAllClassNames() {
        List<String> classNames = classNameQueryService.getAllClassNames();
        return ApiResponse.success(classNames);
    }
}
