package vip.geekclub.internship.adapter.controller;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.geekclub.contract.UserType;
import vip.geekclub.framework.controller.ApiResponse;
import vip.geekclub.framework.controller.WebCommandAdapter;
import vip.geekclub.framework.jooq.PageResult;
import vip.geekclub.framework.security.Authorize;
import vip.geekclub.framework.security.UserPrincipal;
import vip.geekclub.internship.application.command.thesisselection.CreateThesisSelectionCommand;
import vip.geekclub.internship.application.query.*;
import vip.geekclub.internship.application.query.dto.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 论文管理控制器
 * 提供论文及选题相关接口（JSON-RPC 风格命名）
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/internship/thesis")
public class ThesisController {

    private final WebCommandAdapter commandBus;
    private final ThesisQueryService thesisQueryService;
    private final ThesisSelectionQueryService thesisSelectionQueryService;
    private final ThesisSelectionListQueryService thesisSelectionListQueryService;
    private final AdvisorQueryService advisorQueryService;
    private final ClassNameQueryService classNameQueryService;
    private final InternQueryService internQueryService;

    // ==================== 论文相关 ====================

    /**
     * 获取论文列表
     */
    @GetMapping("/list")
    public ApiResponse<List<ThesisListResult>> listThesis() {
        List<ThesisListResult> list = thesisQueryService.getThesisList();
        return ApiResponse.success(list);
    }

    // ==================== 选题相关 ====================

    /**
     * 申请选题
     *
     * @param command 选题命令
     * @param userPrincipal 当前用户
     * @return 操作结果
     */
    @PostMapping("/applySelection")
    @Authorize(userType = UserType.STUDENT)
    public ApiResponse<Void> applySelection(
            @Valid @RequestBody CreateThesisSelectionCommand command,
            UserPrincipal userPrincipal) {
        var internId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        command.setCreatorId(internId);
        return commandBus.dispatchToWeb(command);
    }

    /**
     * 获取当前用户的选题详情
     *
     * @param userPrincipal 当前用户
     * @return 选题详情
     */
    @GetMapping("/getSelectionDetail")
    @Authorize(userType = UserType.STUDENT)
    public ApiResponse<ThesisSelectionDetailResult> getSelectionDetail(UserPrincipal userPrincipal) {
        var internId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        ThesisSelectionDetailResult result = thesisSelectionQueryService.getCurrentUserSelectionDetail(internId);
        return ApiResponse.success(result);
    }

    /**
     * 检查当前用户是否已选题
     *
     * @param userPrincipal 当前用户
     * @return true-已选题，false-未选题
     */
    @GetMapping("/checkSelectionStatus")
    @Authorize(userType = UserType.STUDENT)
    public ApiResponse<Boolean> checkSelectionStatus(UserPrincipal userPrincipal) {
        var internId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        boolean hasSelected = thesisSelectionQueryService.hasCurrentUserSelected(internId);
        return ApiResponse.success(hasSelected);
    }

    /**
     * 获取同指导老师且未选题的学生列表
     *
     * @param userPrincipal 当前用户
     * @return 未选题的学生列表
     */
    @GetMapping("/unselectedStudent")
    @Authorize(userType = UserType.STUDENT)
    public ApiResponse<List<InternInfoResult>> listUnselectedPeers(UserPrincipal userPrincipal) {
        var currentInternId = internQueryService.getInternIdByAuthId(userPrincipal.authId());
        List<InternInfoResult> students = internQueryService.getUnselectedStudentsBySameAdvisor(currentInternId);
        return ApiResponse.success(students);
    }

    /**
     * 获取论文选择结果列表
     *
     * @param query 查询参数（班级、指导老师、学生名字）
     * @return 论文选择结果列表
     */
    @GetMapping("/selectionList")
    @Authorize(userType = UserType.TEACHER)
    public ApiResponse<PageResult<ThesisSelectionListResult>> listThesisSelections(ThesisSelectionListQuery query) {
        PageResult<ThesisSelectionListResult> list = thesisSelectionListQueryService.getThesisSelectionList(query);
        return ApiResponse.success(list);
    }

    /**
     * 获取所有论文选择结果列表（不分页，无条件）
     *
     * @return 所有论文选择结果列表
     */
    @GetMapping("/allSelectionList")
    @Authorize(userType = UserType.TEACHER)
    public ApiResponse<List<ThesisSelectionListResult>> listAllThesisSelections() {
        List<ThesisSelectionListResult> list = thesisSelectionListQueryService.getAllThesisSelectionList();
        return ApiResponse.success(list);
    }

    /**
     * 导出所有论文选择结果为Excel文件（优化版本）
     *
     * @param response HTTP响应对象
     */
    @GetMapping("/allSelectionList/excel")
    @Authorize(userType = UserType.TEACHER)
    public void exportAllThesisSelectionsToExcel(HttpServletResponse response) throws IOException {
        OutputStream outputStream = null;
        try {
            // 获取所有数据
            List<ThesisSelectionListResult> dataList = thesisSelectionListQueryService.getAllThesisSelectionList();
            
            // 生成文件名
            String fileName = "论文选题列表_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
            
            // 设置响应头 - 支持多种浏览器的文件名编码
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("UTF-8");
            
            // 同时设置两种格式的文件名，确保兼容性
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");
            
            // 设置Content-Disposition头，包含两种格式以支持不同浏览器
            response.setHeader("Content-Disposition", 
                "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);
            
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Expires", "0");
            
            // 获取输出流
            outputStream = response.getOutputStream();
            
            // 转换数据并写入
            List<ThesisSelectionExcelDTO> excelDataList = dataList.stream()
                    .map(ThesisSelectionExcelDTO::from)
                    .collect(Collectors.toList());
            
            // 使用EasyExcel写入数据
            EasyExcel.write(outputStream, ThesisSelectionExcelDTO.class)
                    .sheet("论文选题列表")
                    .doWrite(excelDataList);
            
            // 强制刷新缓冲区
            outputStream.flush();
            
        } catch (Exception e) {
            // 记录错误日志
            log.error("Excel导出失败: ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Excel导出失败: " + e.getMessage());
        } finally {
            // 确保资源正确关闭
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("Excel导出资源关闭失败: ", e);
                }
            }
        }
    }

    /**
     * 获取所有指导老师姓名集合（去重）
     *
     * @return 指导老师姓名列表
     */
    @GetMapping("/advisorNames")
    @Authorize(userType = UserType.TEACHER)
    public ApiResponse<List<String>> getAllAdvisorNames() {
        List<String> advisorNames = advisorQueryService.getAllAdvisorNames();
        return ApiResponse.success(advisorNames);
    }

    /**
     * 获取所有班级名称集合（去重）
     *
     * @return 班级名称列表
     */
    @GetMapping("/classNames")
    @Authorize(userType = UserType.TEACHER)
    public ApiResponse<List<String>> getAllClassNames() {
        List<String> classNames = classNameQueryService.getAllClassNames();
        return ApiResponse.success(classNames);
    }
}