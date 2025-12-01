package vip.geekclub.common.utils;

import vip.geekclub.common.exception.ValidationException;

import org.springframework.util.ObjectUtils;

import java.util.function.Supplier;
import java.util.Objects;

/**
 * 断言帮助类
 *
 * @author : leo
 * <p>
 * Ceate Time 2021/5/6 2:15 下午
 * Copyright 2021 www.geekclub.vip Inc. All rights reserved.
 */
public class AssertUtil {

    private AssertUtil() {
        // 工具类禁止实例化
    }

    // ================================ 常用正则表达式 ================================
    
    /** 邮箱正则表达式 */
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    
    /** 手机号正则表达式（中国大陆） */
    private static final String PHONE_REGEX = "^1[3-9]\\d{9}$";
    
    /** 身份证号正则表达式（中国大陆） */
    private static final String ID_CARD_REGEX = "^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    
    /** URL正则表达式 */
    private static final String URL_REGEX = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
    
    /** IPv4地址正则表达式 */
    private static final String IPV4_REGEX = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
    
    /** 用户名正则表达式（字母数字下划线，3-20位） */
    private static final String USERNAME_REGEX = "^[a-zA-Z0-9_]{3,20}$";
    
    /** 密码正则表达式（至少包含字母和数字，6-20位） */
    private static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d@$!%*?&]{6,20}$";
    
    /** 中文姓名正则表达式 */
    private static final String CHINESE_NAME_REGEX = "^[\\u4e00-\\u9fa5]{2,10}$";
    
    /** 邮政编码正则表达式 */
    private static final String POSTAL_CODE_REGEX = "^[1-9]\\d{5}$";

    /* ------------------------- 空值检查 ------------------------- */
    public static void isNull(Object actual, Supplier<String> message) {
        verify(actual == null, () -> new ValidationException(message.get()));
    }

    public static void isNull(Object actual, String message) {
        verify(actual == null, () -> new ValidationException(message));
    }

    public static void notNull(Object actual, Supplier<String> message) {
        verify(actual != null, () -> new ValidationException(message.get()));
    }

    public static void notNull(Object actual, String message) {
        verify(actual != null, () -> new ValidationException(message));
    }

    public static void notEmpty(Object actual, Supplier<String> message) {
        verify(!ObjectUtils.isEmpty(actual), () -> new ValidationException(message.get()));
    }

    public static void notEmpty(Object actual, String message) {
        verify(!ObjectUtils.isEmpty(actual), () -> new ValidationException(message));
    }

    public static void isEmpty(Object actual, Supplier<String> message) {
        verify(ObjectUtils.isEmpty(actual), () -> new ValidationException(message.get()));
    }

    public static void isEmpty(Object actual, String message) {
        verify(ObjectUtils.isEmpty(actual), () -> new ValidationException(message));
    }

    public static void notEmpty(String actual, Supplier<String> message) {
        verify(actual != null && !actual.isBlank(), () -> new ValidationException(message.get()));
    }

    public static void notEmpty(String actual, String message) {
        verify(actual != null && !actual.isBlank(), () -> new ValidationException(message));
    }

    public static void isEmpty(String actual, Supplier<String> message) {
        verify(actual == null || actual.isBlank(), () -> new ValidationException(message.get()));
    }

    public static void isEmpty(String actual, String message) {
        verify(actual == null || actual.isBlank(), () -> new ValidationException(message));
    }

    /* ------------------------- 布尔值检查 ------------------------- */
    public static void isTrue(Boolean actual, Supplier<String> message) {
        verify(Boolean.TRUE.equals(actual), () -> new ValidationException(message.get()));
    }

    public static void isTrue(Boolean actual, String message) {
        verify(Boolean.TRUE.equals(actual), () -> new ValidationException(message));
    }

    public static void isFalse(Boolean actual, Supplier<String> message) {
        verify(Boolean.FALSE.equals(actual), () -> new ValidationException(message.get()));
    }

    public static void isFalse(Boolean actual, String message) {
        verify(Boolean.FALSE.equals(actual), () -> new ValidationException(message));
    }

    /* ------------------------- 相等性检查 ------------------------- */
    public static void isEqual(Object expected, Object actual, Supplier<String> message) {
        verify(Objects.equals(expected, actual), () -> new ValidationException(message.get()));
    }

    public static void isEqual(Object expected, Object actual, String message) {
        verify(Objects.equals(expected, actual), () -> new ValidationException(message));
    }

    public static void notEquals(Object expected, Object actual, Supplier<String> message) {
        verify(!Objects.equals(expected, actual), () -> new ValidationException(message.get()));
    }

    public static void notEquals(Object expected, Object actual, String message) {
        verify(!Objects.equals(expected, actual), () -> new ValidationException(message));
    }

    public static void isEqual(Long expected, Long actual, Supplier<String> message) {
        verify(Objects.equals(expected, actual), () -> new ValidationException(message.get()));
    }

    public static void isEqual(Long expected, Long actual, String message) {
        verify(Objects.equals(expected, actual), () -> new ValidationException(message));
    }

    /* ------------------------- 范围检查 ------------------------- */
    public static void requireLengthBetween(String str, int min, int max, Supplier<String> message) {
        verify(str != null && str.length() >= min && str.length() <= max, () -> new ValidationException(message.get()));
    }

    public static void requireLengthBetween(String str, int min, int max, String message) {
        verify(str != null && str.length() >= min && str.length() <= max, () -> new ValidationException(message));
    }

    public static void requireLengthLessThan(String str, int max, Supplier<String> message) {
        verify(str == null || str.length() <= max, () -> new ValidationException(message.get()));
    }

    public static void requireLengthLessThan(String str, int max, String message) {
        verify(str == null || str.length() <= max, () -> new ValidationException(message));
    }

    public static void requireNumberBetween(Number num, Number min, Number max, Supplier<String> message) {
        verify(num != null && num.doubleValue() >= min.doubleValue() && num.doubleValue() <= max.doubleValue(),
                () -> new ValidationException(message.get()));
    }

    public static void requireNumberBetween(Number num, Number min, Number max, String message) {
        verify(num != null && num.doubleValue() >= min.doubleValue() && num.doubleValue() <= max.doubleValue(),
                () -> new ValidationException(message));
    }

    /* ------------------------- 新增常用断言方法 ------------------------- */
    
    /**
     * 断言对象不为空且不为空白字符串
     */
    public static void notBlank(String actual, String message) {
        verify(actual != null && !actual.trim().isEmpty(), () -> new ValidationException(message));
    }

    public static void notBlank(String actual, Supplier<String> message) {
        verify(actual != null && !actual.trim().isEmpty(), () -> new ValidationException(message.get()));
    }

    /**
     * 断言对象为空或为空白字符串
     */
    public static void isBlank(String actual, String message) {
        verify(actual == null || actual.trim().isEmpty(), () -> new ValidationException(message));
    }

    public static void isBlank(String actual, Supplier<String> message) {
        verify(actual == null || actual.trim().isEmpty(), () -> new ValidationException(message.get()));
    }

    /**
     * 断言数字大于指定值
     */
    public static void greaterThan(Number num, Number threshold, String message) {
        verify(num != null && num.doubleValue() > threshold.doubleValue(), () -> new ValidationException(message));
    }

    public static void greaterThan(Number num, Number threshold, Supplier<String> message) {
        verify(num != null && num.doubleValue() > threshold.doubleValue(), () -> new ValidationException(message.get()));
    }

    /**
     * 断言数字大于等于指定值
     */
    public static void greaterThanOrEqual(Number num, Number threshold, String message) {
        verify(num != null && num.doubleValue() >= threshold.doubleValue(), () -> new ValidationException(message));
    }

    public static void greaterThanOrEqual(Number num, Number threshold, Supplier<String> message) {
        verify(num != null && num.doubleValue() >= threshold.doubleValue(), () -> new ValidationException(message.get()));
    }

    /**
     * 断言数字小于指定值
     */
    public static void lessThan(Number num, Number threshold, String message) {
        verify(num != null && num.doubleValue() < threshold.doubleValue(), () -> new ValidationException(message));
    }

    public static void lessThan(Number num, Number threshold, Supplier<String> message) {
        verify(num != null && num.doubleValue() < threshold.doubleValue(), () -> new ValidationException(message.get()));
    }

    /**
     * 断言数字小于等于指定值
     */
    public static void lessThanOrEqual(Number num, Number threshold, String message) {
        verify(num != null && num.doubleValue() <= threshold.doubleValue(), () -> new ValidationException(message));
    }

    public static void lessThanOrEqual(Number num, Number threshold, Supplier<String> message) {
        verify(num != null && num.doubleValue() <= threshold.doubleValue(), () -> new ValidationException(message.get()));
    }

    /**
     * 断言字符串匹配正则表达式
     */
    public static void matches(String str, String regex, String message) {
        verify(str != null && str.matches(regex), () -> new ValidationException(message));
    }

    public static void matches(String str, String regex, Supplier<String> message) {
        verify(str != null && str.matches(regex), () -> new ValidationException(message.get()));
    }

    /* ------------------------- 格式验证方法 ------------------------- */
    
    /**
     * 断言字符串为有效的邮箱格式
     */
    public static void isEmail(String email, String message) {
        verify(email != null && email.matches(EMAIL_REGEX), () -> new ValidationException(message));
    }

    public static void isEmail(String email, Supplier<String> message) {
        verify(email != null && email.matches(EMAIL_REGEX), () -> new ValidationException(message.get()));
    }

    /**
     * 断言字符串为有效的手机号格式（中国大陆）
     */
    public static void isPhone(String phone, String message) {
        verify(phone != null && phone.matches(PHONE_REGEX), () -> new ValidationException(message));
    }

    public static void isPhone(String phone, Supplier<String> message) {
        verify(phone != null && phone.matches(PHONE_REGEX), () -> new ValidationException(message.get()));
    }

    /**
     * 断言字符串为有效的身份证号格式（中国大陆）
     */
    public static void isIdCard(String idCard, String message) {
        verify(idCard != null && idCard.matches(ID_CARD_REGEX), () -> new ValidationException(message));
    }

    public static void isIdCard(String idCard, Supplier<String> message) {
        verify(idCard != null && idCard.matches(ID_CARD_REGEX), () -> new ValidationException(message.get()));
    }

    /**
     * 断言字符串为有效的URL格式
     */
    public static void isUrl(String url, String message) {
        verify(url != null && url.matches(URL_REGEX), () -> new ValidationException(message));
    }

    public static void isUrl(String url, Supplier<String> message) {
        verify(url != null && url.matches(URL_REGEX), () -> new ValidationException(message.get()));
    }

    /**
     * 断言字符串为有效的IPv4地址格式
     */
    public static void isIpv4(String ip, String message) {
        verify(ip != null && ip.matches(IPV4_REGEX), () -> new ValidationException(message));
    }

    public static void isIpv4(String ip, Supplier<String> message) {
        verify(ip != null && ip.matches(IPV4_REGEX), () -> new ValidationException(message.get()));
    }

    /**
     * 断言字符串为有效的用户名格式
     */
    public static void isUsername(String username, String message) {
        verify(username != null && username.matches(USERNAME_REGEX), () -> new ValidationException(message));
    }

    public static void isUsername(String username, Supplier<String> message) {
        verify(username != null && username.matches(USERNAME_REGEX), () -> new ValidationException(message.get()));
    }

    /**
     * 断言字符串为有效的密码格式
     */
    public static void isPassword(String password, String message) {
        verify(password != null && password.matches(PASSWORD_REGEX), () -> new ValidationException(message));
    }

    public static void isPassword(String password, Supplier<String> message) {
        verify(password != null && password.matches(PASSWORD_REGEX), () -> new ValidationException(message.get()));
    }

    /**
     * 断言字符串为有效的中文姓名格式
     */
    public static void isChineseName(String name, String message) {
        verify(name != null && name.matches(CHINESE_NAME_REGEX), () -> new ValidationException(message));
    }

    public static void isChineseName(String name, Supplier<String> message) {
        verify(name != null && name.matches(CHINESE_NAME_REGEX), () -> new ValidationException(message.get()));
    }

    /**
     * 断言字符串为有效的邮政编码格式
     */
    public static void isPostalCode(String postalCode, String message) {
        verify(postalCode != null && postalCode.matches(POSTAL_CODE_REGEX), () -> new ValidationException(message));
    }

    public static void isPostalCode(String postalCode, Supplier<String> message) {
        verify(postalCode != null && postalCode.matches(POSTAL_CODE_REGEX), () -> new ValidationException(message.get()));
    }

    /* ------------------------- 集合验证方法 ------------------------- */
    
    /**
     * 断言集合不为空
     */
    public static void notEmpty(java.util.Collection<?> collection, String message) {
        verify(collection != null && !collection.isEmpty(), () -> new ValidationException(message));
    }

    public static void notEmpty(java.util.Collection<?> collection, Supplier<String> message) {
        verify(collection != null && !collection.isEmpty(), () -> new ValidationException(message.get()));
    }

    /**
     * 断言集合为空
     */
    public static void isEmpty(java.util.Collection<?> collection, String message) {
        verify(collection == null || collection.isEmpty(), () -> new ValidationException(message));
    }

    public static void isEmpty(java.util.Collection<?> collection, Supplier<String> message) {
        verify(collection == null || collection.isEmpty(), () -> new ValidationException(message.get()));
    }

    /**
     * 断言数组不为空
     */
    public static void notEmpty(Object[] array, String message) {
        verify(array != null && array.length > 0, () -> new ValidationException(message));
    }

    public static void notEmpty(Object[] array, Supplier<String> message) {
        verify(array != null && array.length > 0, () -> new ValidationException(message.get()));
    }

    /**
     * 断言数组为空
     */
    public static void isEmpty(Object[] array, String message) {
        verify(array == null || array.length == 0, () -> new ValidationException(message));
    }

    public static void isEmpty(Object[] array, Supplier<String> message) {
        verify(array == null || array.length == 0, () -> new ValidationException(message.get()));
    }

    /* ------------------------- 业务验证方法 ------------------------- */
    
    /**
     * 断言ID为正数
     */
    public static void isPositiveId(Long id, String message) {
        verify(id != null && id > 0, () -> new ValidationException(message));
    }

    public static void isPositiveId(Long id, Supplier<String> message) {
        verify(id != null && id > 0, () -> new ValidationException(message.get()));
    }

    /**
     * 断言年龄在合理范围内（0-150）
     */
    public static void isValidAge(Integer age, String message) {
        verify(age != null && age >= 0 && age <= 150, () -> new ValidationException(message));
    }

    public static void isValidAge(Integer age, Supplier<String> message) {
        verify(age != null && age >= 0 && age <= 150, () -> new ValidationException(message.get()));
    }

    /**
     * 断言状态值有效
     */
    public static void isValidStatus(String status, String message, String... validStatuses) {
        verify(status != null && java.util.Arrays.asList(validStatuses).contains(status), 
               () -> new ValidationException(message));
    }

    public static void isValidStatus(String status, String[] validStatuses, Supplier<String> message) {
        verify(status != null && java.util.Arrays.asList(validStatuses).contains(status), 
               () -> new ValidationException(message.get()));
    }

    /* ------------------------- 核心验证方法 ------------------------- */

    public static <T extends RuntimeException> void verify(
            Boolean verifyResult,
            Supplier<T> exceptionSupplier) {
        if (Boolean.FALSE.equals(verifyResult)) {
            throw exceptionSupplier.get();
        }
    }
}