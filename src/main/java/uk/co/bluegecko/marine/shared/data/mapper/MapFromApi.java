package uk.co.bluegecko.marine.shared.data.mapper;

public interface MapFromApi<A, D> {

	D fromApi(A wireModel);
}