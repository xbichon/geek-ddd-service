package vip.geekclub.common.utils;

import vip.geekclub.common.exception.ValidationException;

import org.springframework.util.ObjectUtils;

import java.util.function.Supplier;

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

    public static void isFalse(Boolean actual, Supplier<String> message) {
        verify(Boolean.FALSE.equals(actual), () -> new ValidationException(message.get()));
    }

    /* ------------------------- 范围检查 ------------------------- */
    public static void requireLengthBetween(String str, int min, int max, Supplier<String> message) {
        verify(str != null && str.length() >= min && str.length() <= max, () -> new ValidationException(message.get()));
    }

    public static void requireLengthLessThan(String str, int max, Supplier<String> message) {
        verify(str == null || str.length() <= max, () -> new ValidationException(message.get()));
    }

    public static void requireNumberBetween(Number num, Number min, Number max, Supplier<String> message) {
        verify(num != null && num.doubleValue() >= min.doubleValue() && num.doubleValue() <= max.doubleValue(),
                () -> new ValidationException(message.get()));
    }

    public static void notBlank(String actual, Supplier<String> message) {
        verify(actual != null && !actual.trim().isEmpty(), () -> new ValidationException(message.get()));
    }


    /**
     * 断言数字大于指定值
     */
    public static void greaterThan(Number num, Number threshold, String message) {
        verify(num != null && num.doubleValue() > threshold.doubleValue(), () -> new ValidationException(message));
    }


    /* ------------------------- 集合验证方法 ------------------------- */
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
     * 断言数组为空
     */
    public static void isEmpty(Object[] array, String message) {
        verify(array == null || array.length == 0, () -> new ValidationException(message));
    }

    public static void isEmpty(Object[] array, Supplier<String> message) {
        verify(array == null || array.length == 0, () -> new ValidationException(message.get()));
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