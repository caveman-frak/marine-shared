package uk.co.bluegecko.marine.shared.configuration;

import java.time.Clock;
import org.springframework.boot.task.TaskSchedulerBuilder;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

@Configuration
public class SchedulerConfiguration {

	@Bean
	public Clock schedulerClock() {
		return Clock.systemUTC();
	}

	@Bean
	public TaskSchedulerBuilder taskSchedulerBuilder(TaskSchedulerCustomizer... customizers) {
		return new TaskSchedulerBuilder()
				.poolSize(5)
				.threadNamePrefix("scheduler-")
				.awaitTermination(false)
				.customizers(customizers);
	}

	@Bean
	public TaskScheduler taskScheduler(TaskSchedulerBuilder builder, Clock clock) {
		ThreadPoolTaskScheduler scheduler = builder.build();
		scheduler.setClock(clock);
		scheduler.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
		scheduler.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
		scheduler.setWaitForTasksToCompleteOnShutdown(false);
		return scheduler;
	}

}