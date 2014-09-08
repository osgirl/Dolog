package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
			IllegalArgumentException iae = new IllegalArgumentException("Files are invalid. There are no files. No files uploaded perhaps?");
			LogTool.error("Files are invalid. There are no files. No files uploaded perhaps?", iae);
			throw iae;
		}
		
		LogTool.log("Begin search. (" + "Remove Dups: " + removeDuplicates + " Append New Line: " + appendNewLine + ")");
		for (IFileWrapper file : files) {
			LogTool.trace("Searching file", file);
			List<SearchResult> results = new ArrayList<SearchResult>();

			for (BufferedReader br : file.getBufferedReaders()) {
				results.addAll(searchFile(br, query));
			}

			if (removeDuplicates) {
				LogTool.trace("Removing duplicates");
				SearchResult.removeDuplicates(results);
			}

			for (SearchResult result : results) {
				sb.append(result.matchedPart);
				if (appendNewLine) {
					LogTool.trace("Adding new line after result");
					sb.append("\r\n");
				}
			}
		}
		LogTool.log("End search");
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
		LogTool.trace("Begin searching buffered reader with query", query);
		Objects.requireNonNull(br, "Parameters entered are incorrect. Buffered reader is null");
		Objects.requireNonNull(query, "Parameters entered are incorrect. Query is null");
		
		List<SearchResult> results = new ArrayList<SearchResult>();
		ISearcher searcher = new Searcher(10);
		searcher.setFile(br);

		Thread searchThread = searcher.scanDown(query.trim(), -1, (SearchResult result) -> {
			if (result != null) {
				results.add(result);
				LogTool.trace("Result found", result);
			}
		});

		searchThread.join();
		LogTool.trace("Finished searching buffered reader");
		return results;
	}
}
