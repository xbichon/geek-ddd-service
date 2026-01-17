package vip.geekclub.common.controller;

import java.lang.annotation.*;

/**
 * 提交参数
 * @author xiongrui
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface JsonParam {

}