package uk.co.bluegecko.marine.shared.data.mapper;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@MapperConfig(
		componentModel = ComponentModel.SPRING,
		unmappedSourcePolicy = ReportingPolicy.IGNORE,
		unmappedTargetPolicy = ReportingPolicy.IGNORE,
		injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MapperConfiguration {

}