package org.dolan.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dolan.searcher.ISearchCache;

/**
 * The Class SearchCacheFilter.
 * This class filters the search cache so that is generates relevant data in the form of a list.
 */
public class SearchCacheFilter {
	
	/**
	 * Filters the search cache.
	 *
	 * @param lines the lines
	 * @param regexExpression the regex expression
	 * @param terminatingExpression the terminating expression
	 * @return the list
	 */
	public static List<String> filter(ISearchCache<String> lines, String regexExpression, String terminatingExpression) {
		LogTool.trace("Begin filtering lines from line cache", lines);
		LogTool.trace("Using regex expression", regexExpression);
		List<String> filteredLines = new ArrayList<String>();

		for (int i = lines.size() - 1; i > 0; i--) {
			Pattern r = Pattern.compile(regexExpression);
			Matcher m = r.matcher(lines.get(i));

			if (m.find()) {
				filteredLines.add(lines.get(i));

				Pattern terminatingR = Pattern.compile(terminatingExpression);
				Matcher terminatingM = terminatingR.matcher(lines.get(i));
				if (terminatingM.find()) {
					break;
				}
			}
		}
		Collections.reverse(filteredLines);
		LogTool.trace("Finished filtering lines", filteredLines);
		return filteredLines;
	}
}
