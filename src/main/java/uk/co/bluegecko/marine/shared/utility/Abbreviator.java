package uk.co.bluegecko.marine.shared.utility;

import uk.co.bluegecko.marine.shared.utility.abbreviate.DiscardAbbreviator;
import uk.co.bluegecko.marine.shared.utility.abbreviate.PatternAbbreviator;
import uk.co.bluegecko.marine.shared.utility.abbreviate.ShortenAbbreviator;
import uk.co.bluegecko.marine.shared.utility.abbreviate.SimpleAbbreviator;
import uk.co.bluegecko.marine.shared.utility.abbreviate.TruncateAbbreviator;

@FunctionalInterface
public interface Abbreviator {

	String abbreviate(Class<?> klass);

	static Abbreviator none() {
		return Class::getCanonicalName;
	}

	static Abbreviator justClass() {
		return Class::getSimpleName;
	}

	static Abbreviator simple() {
		return new SimpleAbbreviator();
	}

	static Abbreviator keeping(int count) {
		return new DiscardAbbreviator(false, count);
	}

	static Abbreviator discarding(int count) {
		return new DiscardAbbreviator(true, count);
	}

	static Abbreviator truncateExcept(int count) {
		return new TruncateAbbreviator(false, count);
	}

	static Abbreviator truncating(int count) {
		return new TruncateAbbreviator(true, count);
	}

	static Abbreviator shortenExcept(int count, int length) {
		return new ShortenAbbreviator(false, count, length);
	}

	static Abbreviator shortening(int count, int length) {
		return new ShortenAbbreviator(true, count, length);
	}

	static Abbreviator standard() {
		return PatternAbbreviator.standard();
	}

	static Abbreviator pattern(String pattern) {
		return new PatternAbbreviator(pattern);
	}

	static Abbreviator pattern(PatternAbbreviator.Token... tokens) {
		return new PatternAbbreviator(tokens);
	}

}