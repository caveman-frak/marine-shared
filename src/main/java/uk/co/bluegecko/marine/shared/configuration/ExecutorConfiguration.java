package uk.co.bluegecko.marine.shared.configuration;

import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.task.ThreadPoolTaskExecutorBuilder;
import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.ExecutorServiceAdapter;

@Slf4j
public class ExecutorConfiguration {

	@Bean
	public ThreadPoolTaskExecutorBuilder executorBuilder(
			@Value("${marine.task.pool.core:3}") int coreSize,
			@Value("${marine.task.pool.max:10}") int maxSize,
			@Value("${marine.task.capacity:25}") int capacity,
			ThreadPoolTaskExecutorCustomizer... customizers) {
		log.info("Pool size core = {}, max = {}, capacity = {}", coreSize, maxSize, capacity);
		return new ThreadPoolTaskExecutorBuilder()
				.corePoolSize(coreSize)
				.maxPoolSize(maxSize)
				.queueCapacity(capacity)
				.allowCoreThreadTimeOut(true)
				.threadNamePrefix("executor-")
				.awaitTermination(false)
				.customizers(customizers);
	}

	@Bean
	public TaskExecutor executor(ThreadPoolTaskExecutorBuilder builder) {
		return builder.build();
	}

	@Bean
	public ExecutorService executorService(TaskExecutor executor) {
		return new ExecutorServiceAdapter(executor);
	}

}