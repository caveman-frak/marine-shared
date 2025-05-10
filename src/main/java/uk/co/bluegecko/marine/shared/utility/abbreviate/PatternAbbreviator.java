package uk.co.bluegecko.marine.shared.utility.abbreviate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import uk.co.bluegecko.marine.shared.utility.Abbreviator;

public record PatternAbbreviator(Token... tokens) implements Abbreviator {

	public static Abbreviator standard() {
		return new PatternAbbreviator(Token.shortest(), Token.shortened());
	}

	public PatternAbbreviator(String pattern) {
		this(Arrays.stream(pattern.split(" ")).map(Token::new).toArray(Token[]::new));
	}

	@Override
	public String abbreviate(Class<?> klass) {
		StringBuilder buffer = new StringBuilder();
		String[] parts = klass.getPackageName().split("\\.");
		Token[] tokens = tokens(parts.length);

		if (parts.length != tokens.length) {
			throw new IllegalStateException(
					"Number of parts (%d) should equals number of tokens (%d)".formatted(parts.length, tokens.length));
		}
		for (int i = 0; i < parts.length; i++) {
			tokens[i].part(buffer, parts[i]);
		}

		return buffer.append(klass.getSimpleName()).toString();
	}

	Token[] tokens(int parts) {
		// populate initial list by adding each token minimum time, limited by number of parts
		List<Token> result = Arrays.stream(tokens).filter(t -> t.repeat().min() > 0)
				.flatMap(t -> Stream.generate(() -> t).limit(t.repeat().min()))
				.limit(parts).collect(Collectors.toCollection(ArrayList::new));

		// if more tokens are needed then insert tokens that have not been used to max
		if (result.size() < parts) {
			int pos = -1;
			for (Token current : tokens) {
				int first = result.indexOf(current);
				int last = result.lastIndexOf(current);
				if (first > -1) {
					pos = last;
				} else {
					first = pos;
				}
				while (pos - first < current.repeat().length() && result.size() < parts) {
					result.add(++pos, current);
				}
			}
		}

		// finally repeat the last token until we have enough
		while (result.size() < parts) {
			result.add(tokens[tokens().length - 1]);
		}

		return result.toArray(Token[]::new);
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PatternAbbreviator that = (PatternAbbreviator) o;
		return Objects.deepEquals(tokens, that.tokens);
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(tokens);
	}

	public record Token(int chars, char separator, Repeat repeat) {

		public static final char SEPARATOR = '.';

		public Token(String pattern) {
			int chars = pattern.charAt(0) - '0';
			char separator = pattern.charAt(1);
			Repeat repeat = new Repeat(pattern.substring(2));
			this(chars, separator, repeat);
		}

		public static Token shortest() {
			return new Token(1, SEPARATOR, Repeat.wildcard());
		}

		public static Token shortened(int len) {
			return new Token(len, SEPARATOR, Repeat.fixed(2));
		}

		public static Token shortened() {
			return shortened(6);
		}

		public String shorten(String value) {
			return value.substring(0, Math.min(chars, value.length()));
		}

		public void part(StringBuilder builder, String value) {
			builder.append(shorten(value)).append(separator);
		}

	}

	public record Repeat(int min, int max) {

		public static final int NONE = 0;
		public static final int SINGLE = 1;
		public static final int WILDCARD = 99;

		public Repeat(String pattern) {
			int min, max;
			if (pattern.length() == 1) {
				int count = pattern.charAt(0) - '0';
				min = max = count;
			} else {
				min = pattern.charAt(0) - '0';
				char c = pattern.charAt(2);
				max = c == '*' ? WILDCARD : c - '0';
			}
			this(min, max);
		}

		public int length() {
			return max - min;
		}

		public static Repeat repeat(int min, int max) {
			return new Repeat(min, max);
		}

		public static Repeat repeat(int max) {
			return repeat(SINGLE, max);
		}

		public static Repeat once() {
			return repeat(SINGLE, SINGLE);
		}

		public static Repeat fixed(int count) {
			return repeat(count, count);
		}

		public static Repeat optional(int max) {
			return repeat(NONE, max);
		}

		public static Repeat wildcard(int min) {
			return repeat(min, WILDCARD);
		}

		public static Repeat wildcard() {
			return wildcard(NONE);
		}

	}

}