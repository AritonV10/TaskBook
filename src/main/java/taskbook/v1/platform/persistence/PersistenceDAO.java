package taskbook.v1.platform.persistence;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.persistence.EntityManager;



public interface PersistenceDAO {
	
	/**
	 * Persists an entity
	 * @param clasz	the entities class
	 * @param consumer	the callback
	 */
	void save(final Class<?> clasz, final Consumer<EntityManager> consumer);
	
	/**
	 * Updates an entity then returns if the update was a success or not
	 * @param clasz		the return type
	 * @param consumer	the callback
	 * @return	true if the update was a success or otherwise
	 */
	boolean merge(final Class<?> clasz, final Function<EntityManager, Boolean> consumer);
	
	/**
	 * Finds an entity
	 * @param clasz	the entities class
	 * @param bifunction	the callback
	 * @return	{@link Optional} of {@link T} else it contains a null value
	 */
	<T> Optional<T> find(Class<T> clasz, Function<EntityManager, T> bifunction);
	
	/**
	 * Finds a list of {@link T}
	 * @param clasz	the entity class
	 * @param bifunction	the callback
	 * @return	{@link List} of {@link T}
	 */
	<T> List<T> findAll(final Class<?> clasz, final Function<EntityManager, List<T>> bifunction);
	
}
