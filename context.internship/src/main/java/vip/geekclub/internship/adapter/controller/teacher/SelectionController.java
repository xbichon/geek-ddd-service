package vip.geekclub.internship.adapter.controller.teacher;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.support.jooq.PageResult;
import vip.geekclub.support.ExcelExportUtil;
import vip.geekclub.internship.application.query.SelectionListQueryService;
import vip.geekclub.internship.application.query.dto.SelectionPageQuery;
import vip.geekclub.internship.application.query.dto.SelectionItemResult;

import java.io.IOException;
import java.util.List;

@RestController("Teacher_SelectionController")
@RequestMapping("/teacher/internship/selection/")
@RequiredArgsConstructor
public class SelectionController {

    private final SelectionListQueryService thesisSelectionListQueryService;

    /**
     * 获取论文选择结果列表
     *
     * @param query 查询参数（班级、指导老师、学生名字）
     * @return 论文选择结果列表
     */
    @GetMapping("/list")
    public ApiResponse<PageResult<SelectionItemResult>> findPage(SelectionPageQuery query) {
        PageResult<SelectionItemResult> list = thesisSelectionListQueryService.findPage(query);
        return ApiResponse.success(list);
    }

    /**
     * 导出所有论文选择结果为Excel文件
     *
     * @param response HTTP响应对象
     */
    @GetMapping("/export")
    public void exportAllThesisSelectionsToExcel(HttpServletResponse response) throws IOException {
        List<SelectionItemResult> dataList = thesisSelectionListQueryService.findAll();
        ExcelExportUtil.export(response, dataList,SelectionItemResult.class, "论文选题列表", "论文选题列表");
    }
}
