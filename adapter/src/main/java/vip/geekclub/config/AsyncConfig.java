package vip.geekclub.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 异步配置
 *
 * @author : leo
 * <p>
 * Ceate Time 2021/4/26 3:03 下午
 * Copyright 2021 www.geekclub.vip Inc. All rights reserved.
 */
@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    /**
     * 自定义异步线程池，若不重写会使用默认的线程池
     */
    @Override
    @Bean("AsyncExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();

        // 核心线程数
        taskExecutor.setCorePoolSize(2);
        // 最大线程数
        taskExecutor.setMaxPoolSize(10);
        // 队列大小
        taskExecutor.setQueueCapacity(9999);
        // 线程名的前缀
        taskExecutor.setThreadNamePrefix("async-thread-");

        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        taskExecutor.initialize();
        return taskExecutor;
    }
}