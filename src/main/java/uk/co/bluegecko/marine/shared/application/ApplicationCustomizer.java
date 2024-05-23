package uk.co.bluegecko.marine.shared.application;

import java.util.function.Consumer;
import org.springframework.boot.builder.SpringApplicationBuilder;

public interface ApplicationCustomizer extends Consumer<SpringApplicationBuilder> {

}