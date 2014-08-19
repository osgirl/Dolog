package org.dolan.searcher;

import java.util.Queue;

/**
 * The Interface ISearchCache.
 * The search cache is a mechanism to cache the latest search results to enable backwards searching.
 *
 * @param <T> the generic type
 */
public interface ISearchCache<T> extends Queue<T> {
	
	/**
	 * Gets the front of the cache.
	 *
	 * @param index the index
	 * @return the t
	 */
	T get(int index);

	/**
	 * Sets the capacity.
	 *
	 * @param size the new capacity
	 */
	void setCapacity(int size);

	/**
	 * Gets data the from end of the cache.
	 *
	 * @param index the index
	 * @return the from end
	 */
	T getFromEnd(int index);
}
