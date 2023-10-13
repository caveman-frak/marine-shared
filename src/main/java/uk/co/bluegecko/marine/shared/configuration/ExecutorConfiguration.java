package uk.co.bluegecko.marine.shared.configuration;

import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class ExecutorConfiguration {

	@Bean
	public TaskExecutorBuilder taskExecutorBuilder(TaskExecutorCustomizer... customizers) {
		return new TaskExecutorBuilder()
				.corePoolSize(3)
				.maxPoolSize(10)
				.queueCapacity(25)
				.threadNamePrefix("executor-")
				.awaitTermination(false)
				.customizers(customizers);
	}

	@Bean
	TaskExecutor taskExecutor(TaskExecutorBuilder builder) {
		ThreadPoolTaskExecutor executor = builder.build();
		executor.setDaemon(true);
		return executor;
	}

}