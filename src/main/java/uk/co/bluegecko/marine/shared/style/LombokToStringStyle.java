package uk.co.bluegecko.marine.shared.style;

import org.apache.commons.lang3.builder.ToStringStyle;

public class LombokToStringStyle extends ToStringStyle {

	public LombokToStringStyle() {
		setUseShortClassName(true);
		setUseIdentityHashCode(false);
		setContentStart("(");
		setContentEnd(")");
	}

}