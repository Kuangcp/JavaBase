package com.github.kuangcp.pool;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Kuangcp
 * 2025-05-13 15:12
 */
public class SpringThreadPool {

    /**
     * 核心线程数 - 保持较小以减少资源消耗
     */
    private int corePoolSize = 10;

    /**
     * 最大线程数 - 考虑到LLM调用的IO密集特性
     */
    private int maxPoolSize = 20;

    /**
     * 队列容量 - 较大的队列用于缓冲请求
     */
    private int queueCapacity = 1000;

    /**
     * 线程空闲超时时间(秒)
     */
    private int keepAliveSeconds = 300;

    /**
     * 是否等待所有任务完成后再关闭线程池
     */
    private boolean waitForTasksToCompleteOnShutdown = true;

    /**
     * 线程池关闭时等待任务完成的最长时间(秒)
     */
    private int awaitTerminationSeconds = 60;

    @Bean(name = "llmThreadPool")
    public ThreadPoolTaskExecutor llmThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 核心配置
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveSeconds);

        // 使用自定义的线程工厂，设置有意义的线程名称前缀
        executor.setThreadFactory(new BasicThreadFactory.Builder()
                .namingPattern("llm-worker-%d")
                .daemon(true)  // 设置为守护线程
                .build());

        // 设置拒绝策略为CallerRunsPolicy，防止任务丢失
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        // 优雅关闭配置
        executor.setWaitForTasksToCompleteOnShutdown(waitForTasksToCompleteOnShutdown);
        executor.setAwaitTerminationSeconds(awaitTerminationSeconds);

        // 初始化线程池
        executor.initialize();

        return executor;
    }
}
