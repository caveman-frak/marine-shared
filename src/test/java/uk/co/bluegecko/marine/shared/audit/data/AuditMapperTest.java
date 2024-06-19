package uk.co.bluegecko.marine.shared.audit.data;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import uk.co.bluegecko.marine.shared.configuration.TestSharedConfiguration;

@SpringJUnitConfig
@Import(AuditMapperImpl.class)
class AuditMapperTest {

	@Autowired
	AuditMapper mapper;
	@Autowired
	Clock clock;

	Audit audit;
	AuditEvent event;

	@BeforeEach
	void setUp() {
		audit = Audit.builder()
				.id(null)
				.created(clock.instant())
				.principal("some-one")
				.type("some-thing")
				.data(Map.of("foo", "bar"))
				.build();
		event = new AuditEvent(clock.instant(), "some-one", "some-thing", Map.of("foo", "bar"));
	}

	@Test
	void mapFromModelToApi() {
		// no equals implementation, test each field.
		AuditEvent api = mapper.toApi(audit);
		assertThat(api.getPrincipal()).isEqualTo(event.getPrincipal());
		assertThat(api.getTimestamp()).isEqualTo(event.getTimestamp());
		assertThat(api.getType()).isEqualTo(event.getType());
		assertThat(api.getData()).isEqualTo(event.getData());
	}

	@Test
	void mapFromApiToModel() {
		Audit model = mapper.fromApi(event);
		assertThat(model.getId()).describedAs("Id is set").isNotNull();
		assertThat(model).isEqualTo(audit);
	}

	@Configuration
	static class TestConfiguration extends TestSharedConfiguration {

	}

}