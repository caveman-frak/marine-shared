package uk.co.bluegecko.marine.shared.utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

public final class Fmt {

	private Fmt() {
		throw new UnsupportedOperationException();
	}

	private static final Config config = new Config();

	public static Config config() {
		return config;
	}

	public static String fmt(Object arg) {
		return switch (arg) {
			case null -> config().nul();
			case Class<?> c -> fmt(c);
			case Enum<?> e -> fmt(e);
			case Number n -> fmt(n);
			case Stream<?> s -> fmt(s);
			case Collection<?> c -> fmt(c);
			case Map<?, ?> m -> fmt(m);
			case Map.Entry<?, ?> m -> fmt(m);
			case String s when leadingOrTrailingBlank(s) -> config().quote(s);
			case Object a when a.getClass().isArray() -> {
				assert a instanceof Object[];
				yield fmt((Object[]) a);
			}
			case String s -> s;
			default -> fmt(arg.toString());
		};
	}

	private static boolean leadingOrTrailingBlank(String s) {
		int length = s.strip().length();
		return length == 0 || length < s.length();
	}

	public static String fmt(Class<?> arg) {
		return config().abbreviator.abbreviate(arg);
	}

	public static String fmt(Enum<?> arg) {
		return arg.getClass().getSimpleName() + ":" + arg.name();
	}

	public static String fmt(Number arg) {
		return switch (arg) {
			case Byte b -> fmt(b.longValue());
			case Short s -> fmt(s.longValue());
			case Integer i -> fmt(i.longValue());
			case Long l -> fmt(l.longValue());
			case BigInteger i -> fmt(i.longValue());
			case Float f -> fmt(f.doubleValue());
			case Double d -> fmt(d.doubleValue());
			case BigDecimal d -> fmt(d.doubleValue());
			default -> fmt(arg.doubleValue());
		};
	}

	public static String fmt(long arg) {
		return config().longFormat().format(arg);
	}

	public static String fmt(double arg) {
		return config().doubleFormat().format(arg);
	}

	public static String fmt(Stream<?> arg) {
		return fmt(arg, config().stream());
	}

	public static String fmt(Collection<?> arg) {
		return fmt(arg.stream(), config().collection());
	}

	public static String fmt(Map<?, ?> arg) {
		return fmt(arg.entrySet().stream(), config().collection());
	}

	public static String fmt(Map.Entry<?, ?> arg) {
		return fmt(arg.getKey(), arg.getValue());
	}

	public static String fmt(Object key, Object value) {
		return config().kv(fmt(key), fmt(value));
	}

	public static <T> String fmt(T[] arg) {
		return fmt(Stream.of(arg), config().array());
	}

	private static String fmt(Stream<?> arg, Braces braces) {
		return fmt(arg, braces.start(), braces.end());
	}

	public static String fmt(Stream<?> arg, String start, String end) {
		return arg.map(Fmt::fmt).collect(Collectors.joining(config().separator(), start, end));
	}

	public static String hex(int n) {
		return "0x" + Integer.toHexString(n).toUpperCase();
	}

	public static String octal(int n) {
		return "0" + Integer.toOctalString(n);
	}

	public static String binary(int n) {
		return "0b" + Integer.toBinaryString(n);
	}

	@Data
	@Accessors(fluent = true)
	public static class Config {

		private final NumberFormat longFormat = new DecimalFormat("#,##0");
		private final NumberFormat doubleFormat = new DecimalFormat("#,##0.0#####");
		private final Braces collection = new Braces("(", ")");
		private final Braces array = new Braces("[", "]");
		private final Braces stream = new Braces("<", ">");
		private String separator = ",";
		private String entry = "=";
		private String quote = "'";
		private String nul = "NULL";
		private Abbreviator abbreviator = Abbreviator.standard();

		public Config collection(String start, String end) {
			collection().start(start).end(end);
			return this;
		}

		public Config array(String start, String end) {
			array().start(start).end(end);
			return this;
		}

		public Config stream(String start, String end) {
			stream().start(start).end(end);
			return this;
		}

		public String quote(String value) {
			return quote + value + quote;
		}

		public String kv(String key, String value) {
			return key + entry + value;
		}

	}

	@Data
	@AllArgsConstructor
	@Accessors(fluent = true)
	public static class Braces {

		private String start;
		private String end;

	}

}