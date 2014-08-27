package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.play.helpers.IFileWrapper;

import org.dolan.searcher.ISearcher;
import org.dolan.searcher.SearchResult;
import org.dolan.searcher.Searcher;
import org.dolan.tools.LogTool;

// TODO: Auto-generated Javadoc
/**
 * The Class SearcherHelper.
 * This class has all the methods for generic searching a file.
 */
public class SearcherHelper {

	/**
	 * Generic search through the files.
	 *
	 * @param files the files
	 * @param query the query
	 * @param removeDuplicates the remove duplicates
	 * @param appendNewLine the append new line
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static String search(List<IFileWrapper> files, String query, boolean removeDuplicates, boolean appendNewLine) throws IOException, InterruptedException {
		StringBuilder sb = new StringBuilder();
		if (files.size() == 0) {
			return null;
		}
		for (IFileWrapper file : files) {
			List<SearchResult> results = new ArrayList<SearchResult>();

			for (BufferedReader br : file.getBufferedReaders()) {
				results.addAll(searchFile(br, query));
			}

			if (removeDuplicates) {
				SearchResult.removeDuplicates(results);
			}

			for (SearchResult result : results) {
				sb.append(result.matchedPart);
				if (appendNewLine) {
					sb.append("\r\n");
				}
			}
		}
		return sb.toString();
	}

	/**
	 * Search file.
	 *
	 * @param br the buffered reader
	 * @param query the query
	 * @return the list
	 * @throws InterruptedException the interrupted exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static List<SearchResult> searchFile(BufferedReader br, String query) throws InterruptedException, IOException {
		if (br == null || query == null) {
			LogTool.log("BUFFERED READER OR QUERY IS NULL. QUERY IS", query);
		}
		List<SearchResult> results = new ArrayList<SearchResult>();
		ISearcher searcher = new Searcher(10);
		searcher.setFile(br);

		Thread searchThread = searcher.scanDown(query.trim(), -1, (SearchResult result) -> {
			if (result != null)
				results.add(result);
		});

		searchThread.join();
		return results;
	}
}
