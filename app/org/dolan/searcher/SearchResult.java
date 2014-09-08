package org.dolan.searcher;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import org.dolan.tools.LogTool;

/**
 * The Class SearchResult.
 * This class wraps the search result data into a data structure. This is returned after every search.
 */
public class SearchResult {
	
	/** The full line. */
	public String fullLine;
	
	/** The matched part of the result. */
	public String matchedPart;
	
	/** The line number. */
	public int lineNumber;

	/**
	 * Instantiates a new search result.
	 *
	 * @param fullLine the full line
	 * @param matchedPart the matched part
	 * @param lineNumber the line number
	 */
	public SearchResult(String fullLine, String matchedPart, int lineNumber) {
		LogTool.traceC(this.getClass(), "Creating search result", fullLine);
		Objects.requireNonNull(fullLine);
		Objects.requireNonNull(matchedPart);
		if (lineNumber < 0) {
			throw new IllegalArgumentException("Line number cannot be negative");
		}
		this.fullLine = fullLine;
		this.matchedPart = matchedPart;
		this.lineNumber = lineNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "FullLine: " + this.fullLine + " Matched: " + this.matchedPart + " LineNo: " + this.lineNumber;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof SearchResult))
			return false;
		SearchResult s = (SearchResult) obj;
		if (this.matchedPart == s.matchedPart && this.fullLine == s.fullLine && this.lineNumber == s.lineNumber) {
			return true;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int result = 7;

		for (int i = 0; i < this.matchedPart.length(); i++) {
			result = result * 31 + this.matchedPart.charAt(i);
		}

		return result;
	}

	/**
	 * Removes the duplicates in a list of search results.
	 *
	 * @param searchResults the search results
	 */
	public static void removeDuplicates(List<SearchResult> searchResults) {
		LogTool.trace("Begin removing duplicates", searchResults);
		List<SearchResult> outputResults = new ArrayList<SearchResult>();
		HashSet<String> hs = new HashSet<String>();
		for (SearchResult result : searchResults) {
			if (hs.contains(result.matchedPart)) {
				LogTool.trace("Found duplicate", result);
				continue;
			} else {
				outputResults.add(result);
				hs.add(result.matchedPart);
			}
		}
		searchResults.clear();

		for (SearchResult result : outputResults) {
			searchResults.add(result);
		}
		LogTool.trace("Finish removing duplicates", searchResults);
	}
}
