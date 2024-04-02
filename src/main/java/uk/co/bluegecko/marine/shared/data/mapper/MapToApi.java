package uk.co.bluegecko.marine.shared.data.mapper;

public interface MapToApi<A, D> {

	A toApi(D dataModel);

}