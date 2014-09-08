package org.dolan.searcher;

import java.io.BufferedReader;
import java.io.File;
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
		LogTool.traceC(this.getClass(), "Creating Searcher");
		if (cacheSize <= 0) {
			throw new IllegalArgumentException("cache size cannot be zero or a negative number");
		}

		reader = new FileReaderContainer();
		reader.setSearchCache(new SearchCache(cacheSize));
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#setFile(java.io.File)
	 */
	public void setFile(File file) throws IOException {
		LogTool.traceC(this.getClass(), "Setting file", file);
		setFile(new FileReader(file), file.getAbsolutePath());
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#setFile(java.io.BufferedReader)
	 */
	@Override
	public void setFile(BufferedReader br) throws IOException {
		LogTool.traceC(this.getClass(), "Setting file with buffered reader", br);
		if (!br.ready()) {
			IOException ioe = new IOException("BufferedReader is empty in Searcher. Maybe file is unavalible or disconnected from server");
			LogTool.error("BufferedReader is empty in Searcher. Maybe file is unavalible or disconnected from server", ioe);
			throw ioe;
		}
		reader.setReader(br);
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#setFile(java.lang.String)
	 */
	@Override
	public void setFile(String filePath) throws IOException {
		LogTool.traceC(this.getClass(), "Setting file with file path", filePath);
		setFile(new FileReader(filePath), filePath);
	}

	private void setFile(FileReader fr, String filePath) throws IOException {
		LogTool.traceC(this.getClass(), "Setting file with file reader", fr);
		BufferedReader br = new BufferedReader(fr);
		setFile(br);
		reader.setFileName(filePath);
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#scanDown(java.lang.String, int, org.dolan.callbacks.ICallback)
	 */
	@Override
	public Thread scanDown(String query, int amount, ICallback callback) {
		LogTool.traceC(this.getClass(), "searching down with query", query);
		if (reader.getReader() == null) {
			throw new IllegalStateException("Have to specify a file before searching");
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
		LogTool.traceC(this.getClass(), "searching up with query", query);
		if (reader.getReader() == null) {
			throw new IllegalStateException("Have to specify a file before searching");
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
}
