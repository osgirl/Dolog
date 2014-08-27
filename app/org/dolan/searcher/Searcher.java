package org.dolan.searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import org.dolan.callbacks.ICallback;
import org.dolan.tools.LogTool;

/**
 * The Class Searcher.
 * This is an implementation of ISearcher. It is the core of the application. It searches a file up and down and returns results based on the Regex query.
 */
public class Searcher implements ISearcher {

	/** The reader. */
	private IFileReaderContainer reader;

	/**
	 * Instantiates a new searcher.
	 *
	 * @param cacheSize the cache size
	 */
	public Searcher(int cacheSize) {
		reader = new FileReaderContainer();
		reader.setSearchCache(new SearchCache(cacheSize));
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#setFile(java.io.File)
	 */
	public void setFile(File file) throws IOException, FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		if (!br.ready()) {
			throwBufferedReaderNotReadyException();
		}
		reader.setReader(br);
		reader.setFileName(file.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#setFile(java.io.BufferedReader)
	 */
	@Override
	public void setFile(BufferedReader br) throws IOException {
		if (!br.ready()) {
			throwBufferedReaderNotReadyException();
		}
		reader.setReader(br);
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#setFile(java.lang.String)
	 */
	@Override
	public void setFile(String filePath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			reader.setReader(br);
			reader.setFileName(filePath);
		} catch (FileNotFoundException e) {
			LogTool.error("The file cannot be found in the specified path", e);
		}
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#scanDown(java.lang.String, int, org.dolan.callbacks.ICallback)
	 */
	@Override
	public Thread scanDown(String query, int amount, ICallback callback) {
		if (reader.getReader() == null) {
			throwFileNotSpecifiedException();
		}
		
		Thread thread = new Thread(new AsyncSearchDown(reader, Pattern.compile(query), callback, amount));
		thread.start();
		return thread;
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#scanUp(java.lang.String, int, org.dolan.callbacks.ICallback)
	 */
	@Override
	public Thread scanUp(String query, int amount, ICallback callback) {
		if (reader.getReader() == null) {
			throwFileNotSpecifiedException();
		}
		Thread thread = new Thread(new AsyncSearchUp(reader, Pattern.compile(query), callback, amount));
		thread.start();
		return thread;
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#canSearch()
	 */
	@Override
	public boolean canSearch() {
		return reader.hasLines();
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#getCache()
	 */
	@Override
	public SearchCache getCache() {
		return reader.getSearchCache();
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#clearCache()
	 */
	@Override
	public void clearCache() {
		reader.clearSearchCache();
	}
	
	/**
	 * Throw file not specified exception.
	 */
	private void throwFileNotSpecifiedException() {
		IllegalArgumentException iae = new IllegalArgumentException("Didn't specify a file!");
		LogTool.error("Didn't specify a file!", iae);
		throw iae;
	}
	
	/**
	 * Throw buffered reader not ready exception.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void throwBufferedReaderNotReadyException() throws IOException {
		IOException ioe = new IOException("BufferedReader is empty in Searcher. Maybe file is unavalible or disconnected from server");
		LogTool.error("BufferedReader is empty in Searcher. Maybe file is unavalible or disconnected from server", ioe);
		throw ioe;
	}
}
