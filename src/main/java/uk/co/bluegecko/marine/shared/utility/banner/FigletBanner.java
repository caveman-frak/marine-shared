package uk.co.bluegecko.marine.shared.utility.banner;

import static org.springframework.data.util.ParsingUtils.splitCamelCase;

import com.github.dtmo.jfiglet.FigFontResources;
import com.github.dtmo.jfiglet.FigletRenderer;
import java.io.IOException;
import java.io.PrintStream;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.Value;
import org.apache.commons.text.WordUtils;
import org.springframework.boot.Banner;
import org.springframework.core.env.Environment;

@Value(staticConstructor = "custom")
public class FigletBanner implements Banner {

	@NonNull
	String text;

	public static Banner starting() {
		return custom("Starting %s . . .");
	}

	public static Banner running() {
		return custom("%s Running!");
	}

	public static Banner stopping() {
		return custom("Stopping %s . . .");
	}

	@Override
	public void printBanner(Environment environment, Class<?> sourceClass, PrintStream out) {
		String name = environment.getProperty("spring.application.name",
				splitCamelCase(sourceClass.getSimpleName()).stream().map(WordUtils::capitalize)
						.collect(Collectors.joining(" ")));
		String banner = text.formatted(name);

		try {
			FigletRenderer renderer =
					new FigletRenderer(FigFontResources.loadFigFontResource(FigFontResources.STANDARD_FLF));
			out.println(renderer.renderText(banner));
		} catch (IOException e) {
			// fall back to simple text
			out.println(banner);
		}
	}
}