package uk.co.bluegecko.marine.shared.utility.abbreviate;

import uk.co.bluegecko.marine.shared.utility.Abbreviator;

public record ShortenAbbreviator(boolean drop, int number, int length) implements Abbreviator {

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
				int len = Math.min(length, parts[i].length());
				String text = parts[i].substring(0, len);
				buffer.append(text);
			}
			buffer.append(".");
		}
		buffer.append(klass.getSimpleName());

		return buffer.toString();
	}
}