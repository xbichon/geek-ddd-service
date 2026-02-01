package vip.geekclub.framework.command;

import java.lang.annotation.*;

/**
 * 用于在Command类上标注对应的CommandHandler
 * 可以指定一个或多个Handler类
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CommandHandlerMapping {

    /**
     * 指定处理这个Command的Handler类
     * @return Handler类数组
     */
    Class<? extends CommandHandler<?, ?>> value();
}