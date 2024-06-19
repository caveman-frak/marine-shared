package uk.co.bluegecko.marine.shared.audit.data;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MapKeyColumn;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.actuate.audit.AuditEvent;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Audit {

	@Id
	@GeneratedValue
	@EqualsAndHashCode.Exclude
	UUID id;

	@NonNull
	Instant created;

	@NonNull
	@NotBlank
	String principal;

	@NonNull
	@NotBlank
	String type;

	@ElementCollection
	@CollectionTable(name = "audit_data", foreignKey = @ForeignKey(name = "fk_audit"))
	@MapKeyColumn(name = "property")
	@Column(name = "data")
	Map<String, String> data;

	public static Audit from(AuditEvent event) {
		return Audit.builder().id(UUID.randomUUID()).created(event.getTimestamp()).principal(event.getPrincipal()).type(
				event.getType()).data(
				event.getData().entrySet().stream().collect(Collectors.toMap(Entry::getKey,
						e -> e.getValue().toString()))).build();
	}

	public AuditEvent toEvent() {
		return new AuditEvent(getCreated(), getPrincipal(), getType(),
				getData().entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
	}

}