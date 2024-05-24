package uk.co.bluegecko.marine.shared.utility;

import java.util.Optional;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@UtilityClass
@Slf4j
public class Debug {

	public void debug(String message, Object... args) {
		Optional.ofNullable(System.console())
				.ifPresentOrElse(c -> c.printf(message + "\n", args), () -> System.out.printf(message + "\n", args));
		if (log.isDebugEnabled()) {
			log.debug(message, args);
		}
	}
}