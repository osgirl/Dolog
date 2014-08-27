package org.dolan.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dolan.callbacks.ISessionCallback;
import org.dolan.callbacks.IThreadBlockCallback;
import org.dolan.datastructures.ILine;
import org.dolan.datastructures.IThreadBlock;
import org.dolan.datastructures.Line;
import org.dolan.datastructures.ThreadBlock;
import org.dolan.searcher.ISearcher;
import org.dolan.searcher.SearchResult;
import org.dolan.searcher.Searcher;

/**
 * The Class DebenhamsAPISearcher.
 * The implementation of IDebenhamsAPISearcher.
 */
public class DebenhamsAPISearcher implements IDebenhamsAPISearcher {

	/** The searcher. */
	private ISearcher searcher;

	/**
	 * Instantiates a new Debenhams API searcher.
	 *
	 * @param reader the Buffered Reader
	 * @param bufferSize the buffer size
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public DebenhamsAPISearcher(BufferedReader reader, int bufferSize) throws IOException {
		if (!reader.ready()) {
			throwBufferedReaderNotReadyException();
		}
		this.searcher = new Searcher(bufferSize);
		searcher.setFile(reader);
	}

	/**
	 * Instantiates a new Debenhams API searcher.
	 *
	 * @param reader the buffered reader
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public DebenhamsAPISearcher(BufferedReader reader) throws IOException {
		this(reader, 10000);
	}

	/* (non-Javadoc)
	 * @see org.dolan.tools.IDebenhamsAPISearcher#asyncGetThreadBlocksWhichContain(int, org.dolan.callbacks.IThreadBlockCallback)
	 */
	@Override
	public Thread asyncGetThreadBlocksWhichContain(int orderNumber, IThreadBlockCallback callback) throws IOException {
		return asyncFindThreadsWhichContain(Integer.toString(orderNumber), callback);
	}

	/* (non-Javadoc)
	 * @see org.dolan.tools.IDebenhamsAPISearcher#asyncFindThreadsWhichContain(java.lang.String, org.dolan.callbacks.IThreadBlockCallback)
	 */
	@Override
	public Thread asyncFindThreadsWhichContain(String query, IThreadBlockCallback callback) throws IOException {
		List<IThreadBlock> threadBlocks = new ArrayList<IThreadBlock>();

		Thread thread = searcher.scanDown(query, -1, (SearchResult result) -> {
			if (result == null) {
				callback.call(threadBlocks);
			} else {
				ILine line = new Line(result.fullLine, result.lineNumber);
				// "((Post|POST|Get|GET|Delete|DELETE) /[A-Za-z/@_0-9]+ taken\\(ms\\): [0-9]+)"
				// POST /blah taken(ms): 34
				List<String> lines = SearchCacheFilter.filter(searcher.getCache(), "http-executor-threads - " + Integer.toString(line.getThreadNumber()), "((Post|POST|Get|GET|Delete|DELETE) [A-Za-z/@_0-9{}]+)(?=[^A-Za-z/@_0-9{} ])");
				searcher.clearCache();
				IThreadBlock threadBlock = new ThreadBlock();

				for (String currentLine : lines) {
					threadBlock.addLine(currentLine, result.lineNumber);
				}

				threadBlocks.add(threadBlock);
			}
		});

		return thread;
	}

	/* (non-Javadoc)
	 * @see org.dolan.tools.IDebenhamsAPISearcher#asyncGetSessionIDWhichContain(int, org.dolan.callbacks.ISessionCallback)
	 */
	@Override
	public Thread asyncGetSessionIDWhichContain(int orderNumber, ISessionCallback callback) throws IOException {
		List<String> sessionIDs = new ArrayList<String>();
		Thread thread = searcher.scanDown(Integer.toString(orderNumber), -1, (SearchResult result) -> {
			if (result == null) {
				removeDuplicates(sessionIDs);
				callback.call(sessionIDs);
			} else {
				ILine line = new Line(result.fullLine, result.lineNumber);

				List<String> lines = SearchCacheFilter.filter(searcher.getCache(), "http-executor-threads - " + Integer.toString(line.getThreadNumber()), "((Post|POST|Get|GET|Delete|DELETE) [A-Za-z/@_0-9]+ taken\\(ms\\): [0-9]+)");
				searcher.clearCache();

				List<String> newSessionIDs = findSessionIDFromList(lines);
				sessionIDs.addAll(newSessionIDs);

				// searcher.scanDown("...got existing session|...local session updated",
				// 1, callback);
			}
		});

		return thread;
	}

	/**
	 * Find session id from list.
	 *
	 * @param lines the lines
	 * @return the list
	 */
	private List<String> findSessionIDFromList(List<String> lines) {
		List<String> possibleSessionIDs = new ArrayList<String>();
		for (String line : lines) {
			Pattern r = Pattern.compile("...got existing session ([a-zA-Z0-9+]+)|...local session updated ([a-zA-Z0-9+]+)");
			Matcher m = r.matcher(line);

			while (m.find()) {
				String sessionID = null;
				if (m.group(1) == null) {
					sessionID = m.group(2);
				} else {
					sessionID = m.group(1);
				}
				possibleSessionIDs.add(sessionID);
				LogTool.log("FOUND SESSION ID", sessionID);
			}
		}

		return possibleSessionIDs;
	}

	/**
	 * Removes the duplicates.
	 *
	 * @param list the list
	 */
	private void removeDuplicates(List<String> list) {
		HashSet<String> hs = new HashSet<String>();
		hs.addAll(list);
		list.clear();
		list.addAll(hs);
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
