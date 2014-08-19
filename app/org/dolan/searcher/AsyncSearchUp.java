package org.dolan.searcher;

import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dolan.callbacks.ICallback;

/**
 * The Class AsyncSearchUp.
 * It is used to search a file from bottom to top.
 * It is ran in an ISearcher.
 */
public class AsyncSearchUp implements Runnable {

	/** The reader. */
	private IFileReaderContainer reader;
	
	/** The pattern. */
	private Pattern pattern;
	
	/** The callback. */
	private ICallback callback;
	
	/** The amount. */
	private int amount;

	/**
	 * Instantiates a new async search up.
	 *
	 * @param reader the reader
	 * @param pattern the pattern
	 * @param callback the callback
	 * @param amount the amount
	 */
	public AsyncSearchUp(IFileReaderContainer reader, Pattern pattern, ICallback callback, int amount) {
		this.reader = reader;
		this.pattern = pattern;
		this.callback = callback;
		this.amount = amount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int count = 0;

		ListIterator<String> li = reader.getSearchCache().getListIterator();

		mainLoop: while (count != amount) {
			if (!li.hasPrevious())
				break mainLoop;
			String line = li.previous();

			Matcher m = this.pattern.matcher(line);

			reader.setCurrentLineNumber(reader.getCurrentLineNumber() - 1);
			reader.setBackBufferLineNumber(reader.getBackBufferLineNumber() + 1);

			while (m.find()) {
				String matchedPart = null;
				if (m.groupCount() < 1) {
					matchedPart = m.group();
				} else {
					matchedPart = m.group(1);
				}
				SearchResult result = new SearchResult(line, matchedPart, reader.getCurrentLineNumber());
				this.callback.call(result);
				count++;
			}

		}

	}

}
