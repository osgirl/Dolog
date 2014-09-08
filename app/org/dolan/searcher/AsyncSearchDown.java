package org.dolan.searcher;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dolan.callbacks.ICallback;
import org.dolan.tools.LogTool;

/**
 * The Class AsyncSearchDown.
 * It is a Task which will be ran by another Thread.
 * Specifically, it will be ran by the ISearcher.
 * It is used to search a file from top to bottom.
 */
class AsyncSearchDown extends AsyncSearch implements Runnable {
	/**
	 * Instantiates a new top to bottom searcher.
	 *
	 * @param reader the reader
	 * @param pattern the pattern
	 * @param callback the callback
	 * @param amount the amount
	 */
	public AsyncSearchDown(IFileReaderContainer reader, Pattern pattern, ICallback callback, int amount) {
		super(reader, pattern, callback, amount);
		LogTool.traceC(this.getClass(), "Async searcher is a top to bottom searcher");
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		LogTool.traceC(this.getClass(), "Running search thread");
		int count = 0;
		while (count != this.amount) {
			if (this.reader.getBackBufferLineNumber() == 0) {
				break;
			}
			searchFromSearchCache();
			count++;
		}

		String line = null;

		mainLoop: while (count != amount) {
			try {
				line = this.reader.getReader().readLine();
			} catch (IOException e) {
				LogTool.error("Cannot read line from reader.", e);
			}
			if (line == null) {
				this.reader.setHasLines(false);
				break mainLoop;
			}

			this.reader.getSearchCache().add(line);
			Matcher m = this.pattern.matcher(line);

			this.reader.setCurrentLineNumber(reader.getCurrentLineNumber() + 1);

			while (m.find()) {
				String matchedPart = null;
				if (m.groupCount() < 1) {
					matchedPart = m.group();
				} else {
					matchedPart = m.group(1);
				}
				SearchResult result = new SearchResult(line, matchedPart, this.reader.getCurrentLineNumber());
				LogTool.traceC(this.getClass(), "Found match", result);
				this.callback.call(result);
				count++;
			}
		}

		if (line == null) {
			this.reader.setHasLines(false);
			LogTool.traceC(this.getClass(), "Search finished");
			this.callback.call(null); // this will return null at the end of every search. This will notify it is the end of search
		}

	}

	/**
	 * Search from the search cache.
	 */
	private void searchFromSearchCache() {
		LogTool.traceC(this.getClass(), "Searching from cache");
		if (this.reader.getBackBufferLineNumber() < 0) {
			RuntimeException rte = new RuntimeException("At end of queue. Need to start searching from fresh.");
			LogTool.error("At end of queue. Need to start searching from fresh.", rte);
			throw rte;
		}

		String line = this.reader.getSearchCache().getFromEnd(this.reader.getBackBufferLineNumber());
		this.reader.setBackBufferLineNumber(this.reader.getBackBufferLineNumber() - 1);
		Matcher m = this.pattern.matcher(line);

		while (m.find()) {
			String matchedPart = null;
			if (m.groupCount() < 1) {
				matchedPart = m.group();
			} else {
				matchedPart = m.group(1);
			}
			SearchResult sResult = new SearchResult(line, matchedPart, this.reader.getCurrentLineNumber());
			LogTool.traceC(this.getClass(), "Found match", sResult);
			this.callback.call(sResult);
		}
	}
}
