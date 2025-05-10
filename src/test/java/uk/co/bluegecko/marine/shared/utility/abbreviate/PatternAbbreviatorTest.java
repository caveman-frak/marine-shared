package uk.co.bluegecko.marine.shared.utility.abbreviate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.shared.utility.Abbreviator;
import uk.co.bluegecko.marine.shared.utility.abbreviate.PatternAbbreviator.Repeat;
import uk.co.bluegecko.marine.shared.utility.abbreviate.PatternAbbreviator.Token;

class PatternAbbreviatorTest {

	@Test
	void singleToken() {
		Token token = new Token(1, '.', Repeat.once());
		PatternAbbreviator abbreviator = new PatternAbbreviator(token);

		assertThat(abbreviator.tokens(2))
				.describedAs("tokens").hasSize(2).contains(token, token);
		assertThat(abbreviator.abbreviate(List.class))
				.describedAs("abbreviate").isEqualTo("j.u.List");
	}

	@Test
	void singleEmptyToken() {
		Token token = new Token(0, '.', Repeat.once());
		PatternAbbreviator abbreviator = new PatternAbbreviator(token);

		assertThat(abbreviator.tokens(2))
				.describedAs("tokens").hasSize(2).contains(token, token);
		assertThat(abbreviator.abbreviate(List.class))
				.describedAs("abbreviate").isEqualTo("..List");
	}

	@Test
	void twoTokens() {
		Token token1 = new Token(1, '.', Repeat.once());
		Token token2 = new Token(2, ':', Repeat.once());
		PatternAbbreviator abbreviator = new PatternAbbreviator(token1, token2);

		assertThat(abbreviator.tokens(2))
				.describedAs("tokens").hasSize(2).contains(token1, token2);
		assertThat(abbreviator.abbreviate(List.class))
				.describedAs("abbreviate").isEqualTo("j.ut:List");
	}

	@Test
	void twoTokensWithMin() {
		Token token1 = new Token(1, '.', Repeat.repeat(2, 2));
		Token token2 = new Token(2, ':', Repeat.once());
		PatternAbbreviator abbreviator = new PatternAbbreviator(token1, token2);

		assertThat(abbreviator.tokens(2))
				.describedAs("tokens-2").hasSize(2).contains(token1, token1);
		assertThat(abbreviator.abbreviate(List.class))
				.describedAs("abbreviate-2").isEqualTo("j.u.List");
		assertThat(abbreviator.tokens(3))
				.describedAs("tokens-3").hasSize(3).contains(token1, token1, token2);
		assertThat(abbreviator.abbreviate(Stream.class))
				.describedAs("abbreviate-3").isEqualTo("j.u.st:Stream");
	}

	@Test
	void twoTokensWithMax() {
		Token token1 = new Token(1, '.', Repeat.repeat(1, 2));
		Token token2 = new Token(2, ':', Repeat.once());
		PatternAbbreviator abbreviator = new PatternAbbreviator(token1, token2);

		assertThat(abbreviator.tokens(2))
				.describedAs("tokens-2").hasSize(2).contains(token1, token2);
		assertThat(abbreviator.abbreviate(List.class))
				.describedAs("abbreviate-2").isEqualTo("j.ut:List");
		assertThat(abbreviator.tokens(3))
				.describedAs("tokens-3").hasSize(3).contains(token1, token1, token2);
		assertThat(abbreviator.abbreviate(Stream.class))
				.describedAs("abbreviate-3").isEqualTo("j.u.st:Stream");
	}

	@Test
	void threeTokensWithWildcard() {
		Token token1 = new Token(1, '.', Repeat.once());
		Token token2 = new Token(3, ';', Repeat.wildcard());
		Token token3 = new Token(2, ':', Repeat.once());
		PatternAbbreviator abbreviator = new PatternAbbreviator(token1, token2, token3);

		assertThat(abbreviator.tokens(2))
				.describedAs("tokens-2").hasSize(2).contains(token1, token3);
		assertThat(abbreviator.abbreviate(List.class))
				.describedAs("abbreviate-2").isEqualTo("j.ut:List");
		assertThat(abbreviator.tokens(3))
				.describedAs("tokens-3").hasSize(3).contains(token1, token2, token3);
		assertThat(abbreviator.abbreviate(Stream.class))
				.describedAs("abbreviate-3").isEqualTo("j.uti;st:Stream");
		assertThat(abbreviator.tokens(6))
				.describedAs("tokens-6").hasSize(6)
				.contains(token1, token2, token2, token2, token2, token3);
		assertThat(abbreviator.abbreviate(Abbreviator.class))
				.describedAs("abbreviate-6").isEqualTo("u.co;blu;mar;sha;ut:Abbreviator");
	}

	@Test
	void threeTokensWithOptional() {
		Token token1 = new Token(1, '.', Repeat.once());
		Token token2 = new Token(3, ';', Repeat.optional(2));
		Token token3 = new Token(2, ':', Repeat.once());
		PatternAbbreviator abbreviator = new PatternAbbreviator(token1, token2, token3);

		assertThat(abbreviator.tokens(2))
				.describedAs("tokens-2").hasSize(2).contains(token1, token3);
		assertThat(abbreviator.abbreviate(List.class))
				.describedAs("abbreviate-2").isEqualTo("j.ut:List");
		assertThat(abbreviator.tokens(3))
				.describedAs("tokens-3").hasSize(3).contains(token1, token2, token3);
		assertThat(abbreviator.abbreviate(Stream.class))
				.describedAs("abbreviate-3").isEqualTo("j.uti;st:Stream");
		assertThat(abbreviator.tokens(6))
				.describedAs("tokens-6").hasSize(6)
				.contains(token1, token2, token2, token3, token3, token3);
		assertThat(abbreviator.abbreviate(Abbreviator.class))
				.describedAs("abbreviate-6").isEqualTo("u.co;blu;ma:sh:ut:Abbreviator");
	}

	@Test
	void abbreviate() {
		Token token1 = Token.shortest();
		Token token2 = Token.shortened();
		PatternAbbreviator abbreviator = new PatternAbbreviator(token1, token2);

		assertThat(abbreviator.tokens(2))
				.describedAs("tokens-2").hasSize(2).contains(token2, token2);
		assertThat(abbreviator.abbreviate(List.class))
				.describedAs("abbreviate-2").isEqualTo("java.util.List");
		assertThat(abbreviator.tokens(3))
				.describedAs("tokens-3").hasSize(3).contains(token1, token2, token2);
		assertThat(abbreviator.abbreviate(Stream.class))
				.describedAs("abbreviate-3").isEqualTo("j.util.stream.Stream");
		assertThat(abbreviator.tokens(6))
				.describedAs("tokens-6").hasSize(6)
				.contains(token1, token1, token1, token1, token2, token2);
		assertThat(abbreviator.abbreviate(Abbreviator.class))
				.describedAs("abbreviate-6").isEqualTo("u.c.b.m.shared.utilit.Abbreviator");
	}

	@Test
	void standard() {
		Abbreviator abbreviator = PatternAbbreviator.standard();

		assertThat(abbreviator.abbreviate(List.class))
				.describedAs("abbreviate-2").isEqualTo("java.util.List");
		assertThat(abbreviator.abbreviate(Stream.class))
				.describedAs("abbreviate-3").isEqualTo("j.util.stream.Stream");
		assertThat(abbreviator.abbreviate(Abbreviator.class))
				.describedAs("abbreviate-6").isEqualTo("u.c.b.m.shared.utilit.Abbreviator");
	}

	@Test
	void equality() {
		Token shortest = new Token(1, Token.SEPARATOR, Repeat.wildcard());
		Token shortened = new Token(6, Token.SEPARATOR, Repeat.fixed(2));
		PatternAbbreviator abbreviator = new PatternAbbreviator(shortest, shortened);

		assertThat(shortest).describedAs("shortest").isEqualTo(Token.shortest());
		assertThat(shortened).describedAs("shortened").isEqualTo(Token.shortened());
		assertThat(abbreviator).describedAs("standard").isEqualTo(PatternAbbreviator.standard());
	}

	@Test
	void parse() {
		assertThat(new Token("1.2")).describedAs("shortest")
				.isEqualTo(new Token(1, '.', Repeat.fixed(2)));
		assertThat(new Token("2.2:*")).describedAs("shortest")
				.isEqualTo(new Token(2, '.', Repeat.wildcard(2)));
		assertThat(new PatternAbbreviator("1.0:* 6.2")).describedAs("pattern")
				.isEqualTo(PatternAbbreviator.standard());
	}

}