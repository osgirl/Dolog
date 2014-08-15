package org.dolan.searcher;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dolan.callbacks.ICallback;

/**
 * The Class AsyncSearchDown.
 * It is a Task which will be ran by another Thread.
 * Specifically, it will be ran by the ISearcher.
 * It is used to search a file from top to bottom.
 */
class AsyncSearchDown implements Runnable {

	/** The reader. */
	private IFileReaderContainer reader;
	
	/** The callback. */
	private ICallback callback;
	
	/** The search pattern. */
	private Pattern pattern;
	
	/** The amount of lines to search before stopping. */
	private int amount;

	/**
	 * Instantiates a new top to bottom searcher.
	 *
	 * @param reader the reader
	 * @param pattern the pattern
	 * @param callback the callback
	 * @param amount the amount
	 */
	public AsyncSearchDown(IFileReaderContainer reader, Pattern pattern, ICallback callback, int amount) {
		this.reader = reader;
		this.callback = callback;
		this.pattern = pattern;
		this.amount = amount;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		try {
			int count = 0;
			while (count != amount) {
				if (reader.getBackBufferLineNumber() == 0) {
					break;
				}
				searchFromSearchCache();
				count++;
			}

			/*
			 * while (reader.backBufferLineNumber > 0) { result =
			 * searchFromSearchCache(); if (!result.equals("")) { return; } }
			 */

			String line = null;

			mainLoop: while (count != amount) {
				line = this.reader.getReader().readLine();
				if (line == null) {
					reader.setHasLines(false);
					break mainLoop;
				}

				reader.getSearchCache().add(line);
				Matcher m = this.pattern.matcher(line);

				reader.setCurrentLineNumber(reader.getCurrentLineNumber() + 1);

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

			/*
			 * mainLoop: while ((line = this.reader.getReader().readLine()) !=
			 * null) { reader.getSearchCache().add(line); Matcher m =
			 * this.pattern.matcher(line);
			 * 
			 * reader.currentLineNumber++;
			 * 
			 * while (m.find()) { result = m.group(); break mainLoop; }
			 * 
			 * }
			 */

			if (line == null) {
				reader.setHasLines(false);
				//this.callback.call(null); //this will return null at the end of every search. Maybe un-ideal.
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			/*
			 * if (reader.getHasLines()) {
			 * this.callback.call(this.reader.getFileName(),
			 * reader.currentLineNumber, result); } else {
			 * 
			 * 
			 * }
			 */
		}
	}

	/**
	 * Search from the search cache.
	 */
	private void searchFromSearchCache() {
		if (reader.getBackBufferLineNumber() < 0)
			throw new RuntimeException("At end of queue. Need to start searching from fresh.");

		String line = reader.getSearchCache().getFromEnd(reader.getBackBufferLineNumber());
		reader.setBackBufferLineNumber(reader.getBackBufferLineNumber() - 1);
		Matcher m = this.pattern.matcher(line);

		while (m.find()) {
			String matchedPart = null;
			if (m.groupCount() < 1) {
				matchedPart = m.group();
			} else {
				matchedPart = m.group(1);
			}
			SearchResult sResult = new SearchResult(line, matchedPart, reader.getCurrentLineNumber());
			this.callback.call(sResult);
		}
	}
}
