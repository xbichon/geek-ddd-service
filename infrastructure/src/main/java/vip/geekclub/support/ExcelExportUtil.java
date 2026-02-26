package vip.geekclub.support;

import com.alibaba.excel.EasyExcel;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Excel 导出工具类
 * 提供通用的 Excel 文件导出功能
 */
@Slf4j
public class ExcelExportUtil {

    private static final String CONTENT_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    private static final DateTimeFormatter DEFAULT_FILE_NAME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    /**
     * 导出 Excel 文件（自动推断类型）
     * <p>从 dataList 的第一个元素自动推断 Class 类型</p>
     *
     * @param response  HTTP 响应对象
     * @param dataList  Excel 数据列表（不能为空，用于推断类型）
     * @param fileName  文件名（不含扩展名）
     * @param sheetName Sheet 名称
     * @param <R>       Excel DTO 类型
     * @throws IOException              当写入响应流失败时抛出
     * @throws IllegalArgumentException 当 dataList 为空时抛出
     */
    @SuppressWarnings("unchecked")
    public static <R> void export(HttpServletResponse response,
                                  List<R> dataList,
                                  String fileName,
                                  String sheetName) throws IOException {
        if (dataList == null || dataList.isEmpty()) {
            throw new IllegalArgumentException("数据列表不能为空，无法推断 Excel 类型");
        }
        Class<R> excelClass = (Class<R>) dataList.getFirst().getClass();
        export(response, dataList, excelClass, fileName, sheetName);
    }

    /**
     * 导出 Excel 文件（显式指定类型）
     * <p>适用于数据列表可能为空的场景</p>
     *
     * @param response   HTTP 响应对象
     * @param dataList   Excel 数据列表
     * @param excelClass Excel DTO 的 Class 类型
     * @param fileName   文件名（不含扩展名）
     * @param sheetName  Sheet 名称
     * @param <R>        Excel DTO 类型
     * @throws IOException 当写入响应流失败时抛出
     */
    public static <R> void export(HttpServletResponse response,
                                  List<R> dataList,
                                  Class<R> excelClass,
                                  String fileName,
                                  String sheetName) throws IOException {
        OutputStream outputStream = null;
        try {
            // 生成完整文件名
            String fullFileName = fileName + "_" + LocalDateTime.now().format(DEFAULT_FILE_NAME_FORMATTER) + ".xlsx";

            // 设置响应头
            setExcelResponseHeaders(response, fullFileName);

            // 获取输出流
            outputStream = response.getOutputStream();

            // 使用 EasyExcel 写入数据
            EasyExcel.write(outputStream, excelClass)
                    .sheet(sheetName)
                    .doWrite(dataList);

            // 强制刷新缓冲区
            outputStream.flush();

        } catch (Exception e) {
            log.error("Excel 导出失败: ", e);
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("Excel 导出失败: " + e.getMessage());
        } finally {
            // 确保资源正确关闭
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    log.error("Excel 导出资源关闭失败: ", e);
                }
            }
        }
    }

    /**
     * 设置 Excel 导出的 HTTP 响应头
     *
     * @param response HTTP 响应对象
     * @param fileName 完整的文件名（含扩展名）
     */
    private static void setExcelResponseHeaders(HttpServletResponse response, String fileName) {
        // 设置 Content-Type
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");

        // 编码文件名，确保兼容性
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        // 设置 Content-Disposition 头，包含两种格式以支持不同浏览器
        response.setHeader("Content-Disposition",
                "attachment; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName);

        // 设置其他响应头
        response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("Expires", "0");
    }
}