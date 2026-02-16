package vip.geekclub.internship.adapter.controller.teacher;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.jooq.PageResult;
import vip.geekclub.framework.utils.ExcelExportUtil;
import vip.geekclub.internship.application.query.ThesisSelectionListQueryService;
import vip.geekclub.internship.application.query.dto.ThesisSelectionListQuery;
import vip.geekclub.internship.application.query.dto.ThesisSelectionListResult;

import java.io.IOException;
import java.util.List;

@RestController("Teacher_SelectionController")
@RequestMapping("/teacher/internship/selection/")
@RequiredArgsConstructor
public class SelectionController {

    private final ThesisSelectionListQueryService thesisSelectionListQueryService;

    /**
     * 获取论文选择结果列表
     *
     * @param query 查询参数（班级、指导老师、学生名字）
     * @return 论文选择结果列表
     */
    @GetMapping("/list")
    public ApiResponse<PageResult<ThesisSelectionListResult>> listThesisSelections(ThesisSelectionListQuery query) {
        PageResult<ThesisSelectionListResult> list = thesisSelectionListQueryService.getThesisSelectionList(query);
        return ApiResponse.success(list);
    }

    /**
     * 导出所有论文选择结果为Excel文件
     *
     * @param response HTTP响应对象
     */
    @GetMapping("/excel")
    public void exportAllThesisSelectionsToExcel(HttpServletResponse response) throws IOException {
        List<ThesisSelectionListResult> dataList = thesisSelectionListQueryService.getAllThesisSelectionList();
        ExcelExportUtil.export(response, dataList, "论文选题列表", "论文选题列表");
    }
}
