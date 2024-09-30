package uk.co.bluegecko.marine.shared.jassert;

import org.assertj.core.configuration.Configuration;

public class StandardConfiguration extends Configuration {

	public StandardConfiguration() {
		super();

		setLenientDateParsing(true);
		setMaxLengthForSingleLineDescription(120);
	}

}