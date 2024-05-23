package uk.co.bluegecko.marine.shared.application;

import java.util.Set;
import java.util.stream.Stream;
import lombok.NonNull;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.context.WebServerPortFileWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import uk.co.bluegecko.marine.shared.utility.banner.BannerApplicationListener;
import uk.co.bluegecko.marine.shared.utility.banner.FigletBanner;

public abstract class AbstractApplication {

	@Bean
	public ExitCodeGenerator exitCodeGenerator() {
		return () -> 0;
	}

	protected static ApplicationContext run(@NonNull Class<? extends AbstractApplication> sourceClass,
			@NonNull Set<ApplicationListener<?>> listeners, @NonNull WebApplicationType type, @NonNull String[] args,
			@NonNull ApplicationCustomizer... customizers) {
		SpringApplicationBuilder builder = new SpringApplicationBuilder(sourceClass)
				.registerShutdownHook(true)
				.banner(FigletBanner.starting())
				.listeners(applicationListeners(sourceClass, listeners))
				.web(type);
		Stream.of(customizers).forEach(customizer -> customizer.accept(builder));
		return builder.run(args);
	}

	private static ApplicationListener<?>[] applicationListeners(Class<? extends AbstractApplication> sourceClass,
			Set<ApplicationListener<?>> listeners) {
		return Stream.concat(
						Stream.of(new BannerApplicationListener(sourceClass),
								new ApplicationPidFileWriter(),
								new WebServerPortFileWriter()),
						listeners.stream())
				.toArray(i -> new ApplicationListener<?>[i]);
	}

	protected static ApplicationContext run(Class<? extends AbstractApplication> sourceClass, WebApplicationType type,
			String[] args, ApplicationCustomizer... customizers) {
		return run(sourceClass, Set.of(), type, args, customizers);
	}

	protected static ApplicationContext run(Class<? extends AbstractApplication> sourceClass, String[] args,
			ApplicationCustomizer... customizers) {
		return run(sourceClass, Set.of(), WebApplicationType.SERVLET, args, customizers);
	}

	protected static void exit(ApplicationContext context) {
		System.exit(SpringApplication.exit(context));
	}

}