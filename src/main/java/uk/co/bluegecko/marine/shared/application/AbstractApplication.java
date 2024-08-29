package uk.co.bluegecko.marine.shared.application;

import java.util.Collection;
import java.util.Map;
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
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import uk.co.bluegecko.marine.shared.utility.banner.BannerApplicationListener;
import uk.co.bluegecko.marine.shared.utility.banner.FigletBanner;

public abstract class AbstractApplication {

	@Bean
	public ExitCodeGenerator exitCodeGenerator() {
		return () -> 0;
	}

	protected static ConfigurableApplicationContext run(@NonNull Class<? extends AbstractApplication> sourceClass,
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

	protected static ConfigurableApplicationContext run(Class<? extends AbstractApplication> sourceClass, String[] args,
			ApplicationCustomizer... customizers) {
		return run(sourceClass, Set.of(), WebApplicationType.SERVLET, args, customizers);
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

	public static ApplicationCustomizer sources(Class<?>... sources) {
		return a -> a.sources(sources);
	}

	public static ApplicationCustomizer sources(Collection<Class<?>> sources) {
		return a -> a.sources(sources.toArray(Class[]::new));
	}

	public static ApplicationCustomizer properties(Map<String, Object> properties) {
		return a -> a.properties(properties);
	}

	public static ApplicationCustomizer web(WebApplicationType type) {
		return a -> a.web(type);
	}

	public static ApplicationCustomizer profiles(String... profiles) {
		return a -> a.profiles(profiles);
	}

	public static ApplicationCustomizer profiles(Collection<String> profiles) {
		return a -> a.profiles(profiles.toArray(String[]::new));
	}

	protected static void exit(ApplicationContext context) {
		System.exit(SpringApplication.exit(context));
	}

	protected static void close(ConfigurableApplicationContext context) {
		context.close();
	}

}