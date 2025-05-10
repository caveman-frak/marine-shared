package uk.co.bluegecko.marine.shared.utility.abbreviate;

import java.util.Arrays;
import uk.co.bluegecko.marine.shared.utility.Abbreviator;

public record SimpleAbbreviator() implements Abbreviator {

	@Override
	public String abbreviate(Class<?> klass) {
		StringBuilder buffer = new StringBuilder();

		Arrays.stream(klass.getPackageName().split("\\."))
				.map(s -> s.charAt(0))
				.forEach(s -> buffer.append(s).append("."));

		buffer.append(klass.getSimpleName());

		return buffer.toString();
	}
}