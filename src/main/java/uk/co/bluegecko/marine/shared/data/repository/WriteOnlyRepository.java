package uk.co.bluegecko.marine.shared.data.repository;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface WriteOnlyRepository<T, ID> extends ListRepository<T, ID> {

	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed
	 * the entity instance completely.
	 *
	 * @param entity must not be {@literal null}.
	 * @return the saved entity; will never be {@literal null}.
	 * @throws IllegalArgumentException          in case the given {@literal entity} is {@literal null}.
	 * @throws OptimisticLockingFailureException when the entity uses optimistic locking and has a version attribute
	 *                                           with a different value from that found in the persistence store. Also
	 *                                           thrown if the entity is assumed to be present but does not exist in the
	 *                                           database.
	 */
	<S extends T> S save(S entity);

	/**
	 * Saves all given entities.
	 *
	 * @param entities must not be {@literal null} nor must it contain {@literal null}.
	 * @return the saved entities; will never be {@literal null}. The returned {@literal Iterable} will have the same
	 * size as the {@literal Iterable} passed as an argument.
	 * @throws IllegalArgumentException          in case the given {@link Iterable entities} or one of its entities is
	 *                                           {@literal null}.
	 * @throws OptimisticLockingFailureException when at least one entity uses optimistic locking and has a version
	 *                                           attribute with a different value from that found in the persistence
	 *                                           store. Also thrown if at least one entity is assumed to be present but
	 *                                           does not exist in the database.
	 */
	<S extends T> Iterable<S> saveAll(Iterable<S> entities);

}