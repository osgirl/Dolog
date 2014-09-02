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
class AsyncSearchUp extends AsyncSearch implements Runnable {

	/**
	 * Instantiates a new asynchronous bottom to top searcher.
	 *
	 * @param reader the reader
	 * @param pattern the pattern
	 * @param callback the callback
	 * @param amount the amount
	 */
	public AsyncSearchUp(IFileReaderContainer reader, Pattern pattern, ICallback callback, int amount) {
		super(reader, pattern, callback, amount);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		int count = 0;

		ListIterator<String> li = this.reader.getSearchCache().getListIterator();

		mainLoop: while (count != this.amount) {
			if (!li.hasPrevious())
				break mainLoop;
			String line = li.previous();

			Matcher m = this.pattern.matcher(line);

			this.reader.setCurrentLineNumber(this.reader.getCurrentLineNumber() - 1);
			this.reader.setBackBufferLineNumber(this.reader.getBackBufferLineNumber() + 1);

			while (m.find()) {
				String matchedPart = null;
				if (m.groupCount() < 1) {
					matchedPart = m.group();
				} else {
					matchedPart = m.group(1);
				}
				SearchResult result = new SearchResult(line, matchedPart, this.reader.getCurrentLineNumber());
				this.callback.call(result);
				count++;
			}

		}

	}

}
