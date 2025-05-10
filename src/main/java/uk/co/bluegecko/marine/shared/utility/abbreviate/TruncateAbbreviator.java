package uk.co.bluegecko.marine.shared.utility.abbreviate;

import uk.co.bluegecko.marine.shared.utility.Abbreviator;

public record TruncateAbbreviator(boolean drop, int number) implements Abbreviator {

	@Override
	public String abbreviate(Class<?> klass) {
		StringBuilder buffer = new StringBuilder();

		String[] parts = klass.getPackageName().split("\\.");
		for (int i = 0; i < parts.length; i++) {
			if (drop && i >= number) {
				buffer.append(parts[i]);
			} else if (!drop && i >= parts.length - number) {
				buffer.append(parts[i]);
			} else {
				buffer.append(parts[i].charAt(0));
			}
			buffer.append(".");
		}
		buffer.append(klass.getSimpleName());

		return buffer.toString();
	}
}