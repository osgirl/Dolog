package org.dolan.callbacks;

import org.dolan.searcher.SearchResult;

/**
 * The Interface ICallback. 
 * ICallBack is called after each search.
 */
public interface ICallback {
	
	/**
	 * The callback function.
	 *
	 * @param result the search result
	 */
	void call(SearchResult result);
}
