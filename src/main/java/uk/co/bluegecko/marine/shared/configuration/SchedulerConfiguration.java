package uk.co.bluegecko.marine.shared.configuration;

import java.time.Clock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
@Slf4j
public class SchedulerConfiguration {

	@Bean
	public Clock schedulerClock() {
		return Clock.systemUTC();
	}

	@Bean
	public TaskSchedulerBuilder taskSchedulerBuilder(
			@Value("${marine.task.pool.size:5}") int poolSize,
			TaskSchedulerCustomizer... customizers) {
		log.debug("pool size = {}", poolSize);
		return new TaskSchedulerBuilder()
				.poolSize(poolSize)
				.threadNamePrefix("scheduler-")
				.awaitTermination(false)
				.customizers(customizers);
	}

	@Bean
	@Primary
	public TaskScheduler taskScheduler(TaskSchedulerBuilder builder, Clock clock) {
		ThreadPoolTaskScheduler scheduler = builder.build();
		scheduler.setClock(clock);
		scheduler.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		scheduler.setWaitForTasksToCompleteOnShutdown(false);
		return scheduler;
	}

}