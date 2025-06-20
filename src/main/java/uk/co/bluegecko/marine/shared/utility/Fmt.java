package uk.co.bluegecko.marine.shared.utility;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.experimental.Accessors;

public record Fmt(Config config) {

	public static final Fmt fmt = fmt(Config.builder().build());

	static Fmt fmt(Config config) {
		return new Fmt(config);
	}

	public static Fmt fmt(Consumer<Config.ConfigBuilder> customiser) {
		Config.ConfigBuilder builder = fmt.config.toBuilder();
		customiser.accept(builder);
		return new Fmt(builder.build());
	}

	public static String format(Object arg) {
		return fmt.fmt(arg);
	}

	public static String format(String key, Object arg) {
		return fmt.fmt(key, arg);
	}

	public String fmt(Object arg) {
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

	public String fmt(String key, Object arg) {
		return String.format(config.keyValue(), key, fmt(arg));
	}

	private boolean leadingOrTrailingBlank(String s) {
		int length = s.strip().length();
		return length == 0 || length < s.length();
	}

	private String fmt(Class<?> arg) {
		return config().abbreviator.abbreviate(arg);
	}

	private String fmt(Enum<?> arg) {
		StringBuilder buffer = new StringBuilder();
		if (config().showEnumClass()) {
			buffer.append(arg.getClass().getSimpleName()).append(":");
		}
		buffer.append(arg.name());
		return buffer.toString();
	}

	private String fmt(Number arg) {
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

	private String fmt(long arg) {
		return config().longFormat().format(arg);
	}

	private String fmt(double arg) {
		return config().doubleFormat().format(arg);
	}

	private String fmt(Stream<?> arg) {
		return fmt(arg, config().stream());
	}

	private String fmt(Collection<?> arg) {
		return fmt(arg.stream(), config().collection());
	}

	public String fmt(Map<?, ?> arg) {
		return fmt(arg.entrySet().stream(), config().collection());
	}

	private String fmt(Map.Entry<?, ?> arg) {
		return fmt(arg.getKey(), arg.getValue());
	}

	private String fmt(Object key, Object value) {
		return config().kv(fmt(key), fmt(value));
	}

	public <T> String fmt(T[] arg) {
		return fmt(Stream.of(arg), config().array());
	}

	private String fmt(Stream<?> arg, Braces braces) {
		return fmt(arg, braces.start(), braces.end());
	}

	private String fmt(Stream<?> arg, String start, String end) {
		return arg.map(this::fmt).collect(Collectors.joining(config().separator(), start, end));
	}

	public String hex(int n) {
		return "0x" + Integer.toHexString(n).toUpperCase();
	}

	public String octal(int n) {
		return "0" + Integer.toOctalString(n);
	}

	public String binary(int n) {
		return "0b" + Integer.toBinaryString(n);
	}

	@Data
	@Accessors(fluent = true, chain = true)
	@Builder(toBuilder = true)
	public static class Config {

		@Default
		private final NumberFormat longFormat = new DecimalFormat("#,##0");
		@Default
		private final NumberFormat doubleFormat = new DecimalFormat("#,##0.0#####");
		@Default
		private final Braces collection = new Braces("(", ")");
		@Default
		private final Braces array = new Braces("[", "]");
		@Default
		private final Braces stream = new Braces("<", ">");
		@Default
		private String separator = ",";
		@Default
		private String entry = "=";
		@Default
		private String quote = "'";
		@Default
		private String keyValue = "%s=%s";
		@Default
		private String nul = "NULL";
		@Default
		private boolean showEnumClass = true;
		@Default
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