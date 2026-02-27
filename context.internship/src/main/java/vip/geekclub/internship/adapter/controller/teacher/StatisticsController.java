package vip.geekclub.internship.adapter.controller.teacher;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.internship.application.query.InternStatisticsQueryService;
import vip.geekclub.internship.application.query.dto.DailySelectionCountResult;
import vip.geekclub.internship.application.query.dto.InternSelectionStatisticsResult;
import vip.geekclub.internship.application.query.dto.ThesisSelectionCountResult;

import java.util.List;

/**
 * 教师端统计控制器
 * 提供首页图表数据统计
 */
@RestController("Teacher_StatisticsController")
@RequestMapping("/teacher/internship/statistics")
@AllArgsConstructor
public class StatisticsController {

    private final InternStatisticsQueryService statisticsQueryService;

    /**
     * 获取实习生选题统计
     *
     * @return 统计结果
     */
    @GetMapping("/selection")
    public ApiResponse<InternSelectionStatisticsResult> getSelectionStatistics() {
        InternSelectionStatisticsResult result = statisticsQueryService.findSelectionStatistics();
        return ApiResponse.success(result);
    }

    /**
     * 获取论文选题人数统计列表
     * 按已选人数降序排列
     *
     * @return 论文选题人数统计列表
     */
    @GetMapping("/thesis")
    public ApiResponse<List<ThesisSelectionCountResult>> getThesisSelectionCountList() {
        List<ThesisSelectionCountResult> result = statisticsQueryService.findThesisSelectionCountList();
        return ApiResponse.success(result);
    }

    /**
     * 获取每日选题数量统计
     * 按日期分组，统计每天的选题记录数
     *
     * @return 每日选题数量统计列表
     */
    @GetMapping("/daily")
    public ApiResponse<List<DailySelectionCountResult>> getDailySelectionCountList() {
        List<DailySelectionCountResult> result = statisticsQueryService.findDailySelectionCountList();
        return ApiResponse.success(result);
    }
}
