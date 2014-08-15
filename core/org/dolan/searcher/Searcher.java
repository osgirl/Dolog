package org.dolan.searcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

import org.dolan.callbacks.ICallback;

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
	@SuppressWarnings("resource")
	public void setFile(File file) throws IOException, FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		if (!br.ready()) {
			throw new IOException("BufferedReader is empty in Searcher.");
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
			throw new IOException("BufferedReader is empty in Searcher.");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#scanDown(java.lang.String, int, org.dolan.callbacks.ICallback)
	 */
	@Override
	public Thread scanDown(String query, int amount, ICallback callback) {
		if (reader.getReader() == null)
			throw new RuntimeException("Didn't specify a file!");
		Thread thread = new Thread(new AsyncSearchDown(reader, Pattern.compile(query), callback, amount));
		thread.start();
		return thread;
	}

	/* (non-Javadoc)
	 * @see org.dolan.searcher.ISearcher#scanUp(java.lang.String, int, org.dolan.callbacks.ICallback)
	 */
	@Override
	public Thread scanUp(String query, int amount, ICallback callback) {
		if (reader.getReader() == null)
			throw new RuntimeException("Didn't specify a file!");
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
