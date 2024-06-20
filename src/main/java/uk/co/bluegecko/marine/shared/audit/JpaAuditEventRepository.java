package uk.co.bluegecko.marine.shared.audit;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.audit.AuditEvent;
import org.springframework.boot.actuate.audit.AuditEventRepository;
import org.springframework.transaction.annotation.Transactional;
import uk.co.bluegecko.marine.shared.audit.data.Audit;
import uk.co.bluegecko.marine.shared.audit.data.AuditMapper;
import uk.co.bluegecko.marine.shared.audit.data.AuditRepository;
import uk.co.bluegecko.marine.shared.utility.function.NullOrEquals;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class JpaAuditEventRepository implements AuditEventRepository {

	AuditRepository repository;
	AuditMapper mapper;
	Clock clock;

	@Transactional
	@Override
	public void add(AuditEvent event) {
		repository.save(mapper.fromApi(event));
	}

	@Transactional
	@Override
	public List<AuditEvent> find(String principal, Instant after, String type) {
		Instant now = clock.instant();
		Instant since = after != null ? after : now.minus(Duration.ofDays(1));
		return auditStreamOf(principal, type, since, now)
				.filter(filteredBy(principal, type))
				.map(mapper::toApi)
				.toList();
	}

	private Stream<Audit> auditStreamOf(String principal, String type, Instant since, Instant now) {
		if (principal != null) {
			return repository.findByPrincipalAndCreatedBetweenOrderByCreatedDesc(principal, since, now);
		} else if (type != null) {
			return repository.findByTypeAndCreatedBetweenOrderByCreatedDesc(type, since, now);
		} else {
			return repository.findByCreatedBetweenOrderByCreatedDesc(since, now);
		}
	}

	private Predicate<Audit> filteredBy(String principal, String type) {
		return NullOrEquals.nullOrEquals(principal, Audit::getPrincipal)
				.and(NullOrEquals.nullOrEquals(type, Audit::getType));
	}

}