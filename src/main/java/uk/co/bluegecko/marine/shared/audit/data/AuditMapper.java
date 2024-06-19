package uk.co.bluegecko.marine.shared.audit.data;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.boot.actuate.audit.AuditEvent;
import uk.co.bluegecko.marine.shared.data.mapper.MapToAndFromApi;
import uk.co.bluegecko.marine.shared.data.mapper.MapperConfiguration;

@Mapper(config = MapperConfiguration.class, imports = UUID.class)
public interface AuditMapper extends MapToAndFromApi<AuditEvent, Audit> {

	@Mappings({
			@Mapping(target = "created", source = "timestamp"),
			@Mapping(target = "id", expression = "java( UUID.randomUUID() )")})
	@Override
	Audit fromApi(AuditEvent wireModel);

	@Override
	default AuditEvent toApi(Audit dataModel) {
		return new AuditEvent(dataModel.getCreated(), dataModel.getPrincipal(), dataModel.getType(),
				dataModel.getData().entrySet().stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue)));
	}

	default String toString(Object from) {
		return from.toString();
	}

}