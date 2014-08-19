package org.dolan.searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.dolan.callbacks.ICallback;

/**
 * The Interface ISearcher.
 * This is the core of the application. It searches a file up or down asynchronously and returns the results.
 */
public interface ISearcher {
	
	/**
	 * Sets the file.
	 *
	 * @param file the new file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws FileNotFoundException the file not found exception
	 */
	public void setFile(File file) throws IOException, FileNotFoundException;

	/**
	 * Sets the file's BufferedReader.
	 *
	 * @param br the new file's BufferedReader
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void setFile(BufferedReader br) throws IOException;

	/**
	 * Sets the file based on it's file path.
	 *
	 * @param filePath the new file's file path
	 */
	public void setFile(String filePath);

	/**
	 * Scan down.
	 *
	 * @param query the query
	 * @param amount the amount
	 * @param callback the callback
	 * @return the thread
	 */
	public Thread scanDown(String query, int amount, ICallback callback);

	/**
	 * Scan up.
	 *
	 * @param query the query
	 * @param amount the amount
	 * @param callback the callback
	 * @return the thread
	 */
	public Thread scanUp(String query, int amount, ICallback callback);

	/**
	 * Check if its still possible to search. If it is at the end of the file or not.
	 *
	 * @return true, if successful
	 */
	public boolean canSearch();

	/**
	 * Gets the search cache.
	 *
	 * @return the cache
	 */
	public SearchCache getCache();

	/**
	 * Clear the search cache.
	 */
	public void clearCache();
}
