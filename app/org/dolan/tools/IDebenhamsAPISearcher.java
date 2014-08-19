package org.dolan.tools;

import java.io.IOException;

import org.dolan.callbacks.ISessionCallback;
import org.dolan.callbacks.IThreadBlockCallback;

/**
 * The Interface IDebenhamsAPISearcher.
 * This Searcher uses the ISearcher to search through Debenhams API log files.
 * It is all asynchronous and returns ThreadBlocks.
 * 
 */
public interface IDebenhamsAPISearcher {
	
	/**
	 * Asynchronously get thread blocks which contain order number.
	 *
	 * @param orderNumber the order number
	 * @param callback the callback
	 * @return the thread
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Thread asyncGetThreadBlocksWhichContain(int orderNumber, IThreadBlockCallback callback) throws IOException;

	/**
	 * Asynchronously find threads which contain query.
	 *
	 * @param query the query
	 * @param callback the callback
	 * @return the thread
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Thread asyncFindThreadsWhichContain(String query, IThreadBlockCallback callback) throws IOException;

	/**
	 * Asynchronously get session id which contain order number.
	 *
	 * @param orderNumber the order number
	 * @param callback the callback
	 * @return the thread
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public Thread asyncGetSessionIDWhichContain(int orderNumber, ISessionCallback callback) throws IOException;

}
