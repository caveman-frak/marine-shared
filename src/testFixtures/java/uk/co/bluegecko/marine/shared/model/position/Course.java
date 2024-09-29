package uk.co.bluegecko.marine.shared.model.position;

import java.util.function.UnaryOperator;

public interface Course {

	UnaryOperator<Trace> next();

}