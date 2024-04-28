package uk.co.bluegecko.marine.shared.configuration;

import java.time.Clock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.task.ThreadPoolTaskSchedulerBuilder;
import org.springframework.boot.task.ThreadPoolTaskSchedulerCustomizer;
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
	public ThreadPoolTaskSchedulerBuilder taskSchedulerBuilder(
			@Value("${marine.task.pool.size:5}") int poolSize,
			ThreadPoolTaskSchedulerCustomizer... customizers) {
		log.info("Pool size = {}", poolSize);
		return new ThreadPoolTaskSchedulerBuilder()
				.poolSize(poolSize)
				.threadNamePrefix("scheduler-")
				.awaitTermination(false)
				.customizers(customizers);
	}

	@Bean
	@Primary
	public TaskScheduler taskScheduler(ThreadPoolTaskSchedulerBuilder builder, Clock clock) {
		ThreadPoolTaskScheduler scheduler = builder.build();
		scheduler.setClock(clock);
		scheduler.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		scheduler.setWaitForTasksToCompleteOnShutdown(false);
		return scheduler;
	}

}