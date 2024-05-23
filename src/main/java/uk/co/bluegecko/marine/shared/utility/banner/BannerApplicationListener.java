package uk.co.bluegecko.marine.shared.utility.banner;

import lombok.NonNull;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.Banner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

@Value
@Slf4j
public class BannerApplicationListener implements ApplicationListener<ApplicationEvent> {

	Class<?> sourceClass;

	@Override
	public void onApplicationEvent(@NonNull ApplicationEvent event) {
		if (event instanceof ApplicationReadyEvent e) {
			Banner banner = FigletBanner.running();
			banner.printBanner(e.getApplicationContext().getEnvironment(), sourceClass, System.out);
		} else if (event instanceof ContextClosedEvent e) {
			Banner banner = FigletBanner.stopping();
			banner.printBanner(e.getApplicationContext().getEnvironment(), sourceClass, System.out);
		}
	}
}