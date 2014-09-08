package org.dolan.searcher;

import java.util.Objects;
import java.util.regex.Pattern;

import org.dolan.callbacks.ICallback;
import org.dolan.tools.LogTool;

/**
 * The Class AsyncSearch.
 * Search objects should extend this
 */
abstract class AsyncSearch {
	/** The reader. */
	protected IFileReaderContainer reader;

	/** The callback. */
	protected ICallback callback;

	/** The search pattern. */
	protected Pattern pattern;

	/** The amount of lines to search before stopping. */
	protected int amount;
	
	/**
	 * Instantiates a new asynchronous searcher.
	 *
	 * @param reader the reader
	 * @param pattern the pattern
	 * @param callback the callback
	 * @param amount the amount
	 */
	public AsyncSearch(IFileReaderContainer reader, Pattern pattern, ICallback callback, int amount) {
		LogTool.traceC(this.getClass(), "Creating async searcher");
		Objects.requireNonNull(reader);
		Objects.requireNonNull(callback);
		Objects.requireNonNull(pattern);
		if (amount == 0) {
			throw new IllegalArgumentException("Value of search amount cannot be 0");
		}
		this.reader = reader;
		this.callback = callback;
		this.pattern = pattern;
		this.amount = amount;
	}
}
