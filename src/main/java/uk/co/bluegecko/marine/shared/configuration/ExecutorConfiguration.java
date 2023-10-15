package uk.co.bluegecko.marine.shared.configuration;

import java.util.concurrent.ExecutorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.ExecutorServiceAdapter;

@Configuration
@Slf4j
public class ExecutorConfiguration {

	@Bean
	public TaskExecutorBuilder taskExecutorBuilder(
			@Value("${marine.task.pool.core:3}") int coreSize,
			@Value("${marine.task.pool.max:10}") int maxSize,
			@Value("${marine.task.capacity:25}") int capacity,
			TaskExecutorCustomizer... customizers) {
		log.debug("pool size core = {}, max = {}, capacity = {}", coreSize, maxSize, capacity);
		return new TaskExecutorBuilder()
				.corePoolSize(coreSize)
				.maxPoolSize(maxSize)
				.queueCapacity(capacity)
				.allowCoreThreadTimeOut(true)
				.threadNamePrefix("executor-")
				.awaitTermination(false)
				.customizers(customizers);
	}

	@Bean
	public TaskExecutor taskExecutor(TaskExecutorBuilder builder) {
		return builder.build();
	}

	@Bean
	public ExecutorService executorService(TaskExecutor executor) {
		return new ExecutorServiceAdapter(executor);
	}

}