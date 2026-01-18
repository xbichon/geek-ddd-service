package vip.geekclub.framework.utils;

import lombok.NonNull;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author xiongrui
 */
@Component
public class ApplicationUtil implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) {
        setContext(applicationContext);
    }

    private static void setContext(ApplicationContext context) {
        applicationContext = context;
    }

    private static ApplicationContext getContext() {
        return applicationContext;
    }

    public static <T> T getBean(Class<T> bean) {
        return getContext().getBean(bean);
    }
}