package org.dolan.searcher;

import java.io.BufferedReader;
import java.util.Objects;

import org.dolan.tools.LogTool;

/**
 * The Class FileReaderContainer.
 */
class FileReaderContainer implements IFileReaderContainer {

	/** The reader. */
	private BufferedReader reader;
	
	/** The file name. */
	private String fileName;
	
	/** The search cache. */
	private SearchCache searchCache;
	
	/** The has more lines. */
	private boolean hasMoreLines;

	/** The current line number. */
	private int currentLineNumber = 0;
	
	/** The back buffer line number. */
	private int backBufferLineNumber = 0;

	/**
	 * Instantiates a new file reader container.
	 *
	 * @param reader the reader
	 * @param fileName the file name
	 * @param searchCache the search cache
	 */
	public FileReaderContainer(BufferedReader reader, String fileName, SearchCache searchCache) {
		this();
		Objects.requireNonNull(reader);
		Objects.requireNonNull(fileName);
		Objects.requireNonNull(searchCache);
		this.reader = reader;
		this.fileName = fileName;
		this.searchCache = searchCache;
	}

	/**
	 * Instantiates a new file reader container.
	 */
	public FileReaderContainer() {
		LogTool.traceC(this.getClass(), "Creating FileReaderContainerBuffer");
		this.hasMoreLines = true;
	}

	/**
	 * Sets the reader.
	 *
	 * @param reader the new reader
	 */
	@Override
	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	/**
	 * Sets the file name.
	 *
	 * @param fileName the new file name
	 */
	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Sets the search cache.
	 *
	 * @param searchCache the new search cache
	 */
	@Override
	public void setSearchCache(SearchCache searchCache) {
		this.searchCache = searchCache;
	}

	/**
	 * Gets the reader.
	 *
	 * @return the reader
	 */
	@Override
	public BufferedReader getReader() {
		return this.reader;
	}

	/**
	 * Gets the file name.
	 *
	 * @return the file name
	 */
	@Override
	public String getFileName() {
		return fileName;
	}

	/**
	 * Gets the search cache.
	 *
	 * @return the search cache
	 */
	@Override
	public SearchCache getSearchCache() {
		return searchCache;
	}

	/**
	 * Clear search cache.
	 */
	@Override
	public void clearSearchCache() {
		searchCache.clear();
	}

	/**
	 * Sets the checks for lines.
	 *
	 * @param hasLines the new checks for lines
	 */
	@Override
	public void setHasLines(boolean hasLines) {
		this.hasMoreLines = hasLines;
	}

	/**
	 * Checks for lines.
	 *
	 * @return true, if successful
	 */
	@Override
	public boolean hasLines() {
		return this.hasMoreLines;
	}

	/**
	 * Gets the current line number.
	 *
	 * @return the current line number
	 */
	@Override
	public int getCurrentLineNumber() {
		return this.currentLineNumber;
	}

	/**
	 * Gets the back buffer line number.
	 *
	 * @return the back buffer line number
	 */
	@Override
	public int getBackBufferLineNumber() {
		return this.backBufferLineNumber;
	}

	/**
	 * Sets the current line number.
	 *
	 * @param currentLineNumber the new current line number
	 */
	@Override
	public void setCurrentLineNumber(int currentLineNumber) {
		this.currentLineNumber = currentLineNumber;
	}

	/**
	 * Sets the back buffer line number.
	 *
	 * @param backBufferLineNumber the new back buffer line number
	 */
	@Override
	public void setBackBufferLineNumber(int backBufferLineNumber) {
		this.backBufferLineNumber = backBufferLineNumber;
	}
}
