package uk.co.bluegecko.marine.shared.data.repository;


import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Interface for generic recall operations on a repository for a specific type.
 * This an extension to Repository returning List instead of Iterable where applicable.
 *
 * @param <T>  the entity type.
 * @param <ID> the key type.
 */
@NoRepositoryBean
public interface ListRepository<T, ID> extends Repository<T, ID> {

	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal Optional#empty()} if none found.
	 * @throws IllegalArgumentException if {@literal id} is {@literal null}.
	 */
	Optional<T> findById(ID id);

	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param id must not be {@literal null}.
	 * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@literal id} is {@literal null}.
	 */
	boolean existsById(ID id);

	/**
	 * Returns all instances of the type.
	 *
	 * @return all entities
	 */
	List<T> findAll();

	/**
	 * Returns all instances of the type {@code T} with the given IDs.
	 * <p>
	 * If some or all ids are not found, no entities are returned for these IDs.
	 * <p>
	 * Note that the order of elements in the result is not guaranteed.
	 *
	 * @param ids must not be {@literal null} nor contain any {@literal null} values.
	 * @return guaranteed to be not {@literal null}. The size can be equal or less than the number of given
	 * {@literal ids}.
	 * @throws IllegalArgumentException in case the given {@link Iterable ids} or one of its items is {@literal null}.
	 */
	List<T> findAllById(Iterable<ID> ids);

	/**
	 * Returns the number of entities available.
	 *
	 * @return the number of entities.
	 */
	long count();

}
