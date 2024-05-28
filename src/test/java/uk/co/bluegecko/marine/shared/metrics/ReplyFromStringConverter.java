package uk.co.bluegecko.marine.shared.metrics;

import io.micrometer.core.instrument.config.MeterFilterReply;
import org.junit.jupiter.params.converter.TypedArgumentConverter;

public class ReplyFromStringConverter extends TypedArgumentConverter<String, MeterFilterReply> {

	protected ReplyFromStringConverter() {
		super(String.class, MeterFilterReply.class);
	}

	@Override
	protected MeterFilterReply convert(String source) {
		return switch (source) {
			case "accept" -> MeterFilterReply.ACCEPT;
			case "deny" -> MeterFilterReply.DENY;
			case "neutral" -> MeterFilterReply.NEUTRAL;
			default -> throw new IllegalArgumentException("Unable to convert \"" + source + "\" to a reply");
		};
	}
}