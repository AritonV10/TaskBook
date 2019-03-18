package taskbook.v1.platform.utility;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collector;

public interface BinaryTree<T extends Comparable<T>> extends Collector<T, List<T>, BinaryTree<T>>{

	/**
	 * Search for a entity in the tree
	 * Use if only the {@link T} implemented {@link Comparable<T>} interface
	 * @param value
	 * @return value or null if it doesn't exist
	 */
	T get(T value);
	
	/**
	 * Add a value
	 * @param value
	 * @return if the value was successfully added or not
	 */
	boolean add(T value);
	
	/**
	 * Return a {@link List}
	 * @return {@link List} of {@link T}
	 */
	List<T> toList();
	
	/**
	 * Adds the the {@link T} entities to the supplied {@link List}
	 * @param supplier	the supplied {@link List}
	 * @return	{@link List} of {@link T}
	 */
	List<T> toList(Supplier<List<T>> supplier);
	
	/**
	 * For testing
	 */
	void display(Consumer<T> consumer);
	
	/**
	 * Tree's size
	 * @return returns size
	 */
	Integer size();
}
