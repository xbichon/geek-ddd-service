package vip.geekclub.common.utils;

import org.springframework.util.StringUtils;

/**
 * 字符串工具类
 * 结合Spring Framework和Java内置方法的优势
 * 
 * @author system
 */
public final class StringUtil {
    
    private StringUtil() {
        // 工具类不允许实例化
    }
    
    /**
     * 去除首尾空白，空字符串转换为null
     * 等价于Apache Commons Lang3的trimToNull
     * 
     * @param str 输入字符串
     * @return 处理后的字符串，null或空字符串返回null
     */
    public static String trimToNull(String str) {
        return StringUtils.hasText(str) ? str.strip() : null;
    }
    
    /**
     * 去除首尾空白，保持空字符串
     * 
     * @param str 输入字符串
     * @return 处理后的字符串
     */
    public static String trim(String str) {
        return str != null ? str.strip() : null;
    }
    
    /**
     * 检查字符串是否为空（null或空字符串）
     * 
     * @param str 输入字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return !StringUtils.hasText(str);
    }
    
    /**
     * 检查字符串是否不为空
     * 
     * @param str 输入字符串
     * @return 是否不为空
     */
    public static boolean isNotEmpty(String str) {
        return StringUtils.hasText(str);
    }
    
    /**
     * 检查字符串是否为空白（null、空字符串或只包含空白字符）
     * 
     * @param str 输入字符串
     * @return 是否为空白
     */
    public static boolean isBlank(String str) {
        return str == null || str.isBlank();
    }
    
    /**
     * 检查字符串是否不为空白
     * 
     * @param str 输入字符串
     * @return 是否不为空白
     */
    public static boolean isNotBlank(String str) {
        return str != null && !str.isBlank();
    }
}
