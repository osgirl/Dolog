package org.dolan.searcher;

import java.io.BufferedReader;

/**
 * The Interface IFileReaderContainer.
 * This file container is a wrapper for the BufferedReader and the Cache used for searching.
 */
public interface IFileReaderContainer {
	
	/**
	 * Sets the reader.
	 *
	 * @param reader the new reader
	 */
	public void setReader(BufferedReader reader);

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	public void setFileName(String fileName);

	/**
	 * Sets the search cache.
	 *
	 * @param searchCache the new search cache
	 */
	public void setSearchCache(SearchCache searchCache);

	/**
	 * Gets the reader.
	 *
	 * @return the reader
	 */
	public BufferedReader getReader();

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	public String getFileName();

	/**
	 * Gets the search cache.
	 *
	 * @return the search cache
	 */
	public SearchCache getSearchCache();

	/**
	 * Clear search cache.
	 */
	public void clearSearchCache();

	/**
	 * Sets the checks for lines.
	 *
	 * @param hasLines the new checks for lines
	 */
	public void setHasLines(boolean hasLines);

	/**
	 * Checks for lines.
	 *
	 * @return true, if successful
	 */
	public boolean hasLines();
	
	/**
	 * Gets the current line number.
	 *
	 * @return the current line number
	 */
	public int getCurrentLineNumber();
	
	/**
	 * Sets the current line number.
	 *
	 * @param currentLineNumber the new current line number
	 */
	public void setCurrentLineNumber(int currentLineNumber);
	
	/**
	 * Gets the back buffer line number.
	 *
	 * @return the back buffer line number
	 */
	public int getBackBufferLineNumber();
	
	/**
	 * Sets the back buffer line number.
	 *
	 * @param backBufferLineNumber the new back buffer line number
	 */
	public void setBackBufferLineNumber(int backBufferLineNumber);
}
