package org.dolan.searcher;

import java.io.BufferedReader;

class FileReaderContainer implements IFileReaderContainer {

	private BufferedReader reader;
	private String fileName;
	private SearchCache searchCache;
	private boolean hasMoreLines;

	private int currentLineNumber = 0;
	private int backBufferLineNumber = 0;

	public FileReaderContainer(BufferedReader reader, String fileName, SearchCache searchCache) {
		this();
		this.reader = reader;
		this.fileName = fileName;
		this.searchCache = searchCache;
	}

	public FileReaderContainer() {
		this.hasMoreLines = true;
	}

	@Override
	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public void setSearchCache(SearchCache searchCache) {
		this.searchCache = searchCache;
	}

	@Override
	public BufferedReader getReader() {
		return this.reader;
	}

	@Override
	public String getFileName() {
		return fileName;
	}

	@Override
	public SearchCache getSearchCache() {
		return searchCache;
	}

	@Override
	public void clearSearchCache() {
		searchCache.clear();
	}

	@Override
	public void setHasLines(boolean hasLines) {
		this.hasMoreLines = hasLines;
	}

	@Override
	public boolean hasLines() {
		return this.hasMoreLines;
	}

	@Override
	public int getCurrentLineNumber() {
		return this.currentLineNumber;
	}

	@Override
	public int getBackBufferLineNumber() {
		return this.backBufferLineNumber;
	}

	@Override
	public void setCurrentLineNumber(int currentLineNumber) {
		this.currentLineNumber = currentLineNumber;
	}

	@Override
	public void setBackBufferLineNumber(int backBufferLineNumber) {
		this.backBufferLineNumber = backBufferLineNumber;
	}
}
