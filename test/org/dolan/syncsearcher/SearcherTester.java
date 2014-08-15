package org.dolan.syncsearcher;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.dolan.searcher.SearchResult;
import org.dolan.syncsearcher.Searcher;
import org.junit.Ignore;
import org.junit.Test;

public class SearcherTester {

	@Ignore
	@Test
	public void TestIfSearcherSearchesWithExpression() {
		Searcher searcher = new Searcher();
		searcher.load("C:\\log.txt");

		List<SearchResult> results = searcher.search("(OS Information)", 1);
		System.out.println(results.toString());
		assertEquals(" OS Information [WINMAJOR Number] = 6", results.get(0).fullLine);
		assertEquals("OS Information", results.get(0).matchedPart);
		assertEquals(6, results.get(0).lineNumber);

	}

	@Test
	// LUKETEST
	// THIS SCRIPT IS BUGGY, NEEDS BRACKETS TO WORK.
	public void TestIfSearcherSearchesForMultipleValue() {
		Searcher s = new Searcher();
		// s.load("C:\\jiralog.txt");
		s.load("C:\\Inventory_ERROR_2014.07.28_05.12.12.545.log");
		List<SearchResult> results = s.search("index data \\[([A-Z\\\\0-9]+), [0-9]+\\]\\.", -1);
		SearchResult.removeDuplicates(results);

		for (SearchResult result : results) {
			System.out.println(result.matchedPart);
		}

	}

	@Ignore
	@Test
	public void TestIfSearcherSearchesForCSV() {
		Searcher s = new Searcher();
		// s.load("C:\\jiralog.txt");
		s.load("C:\\jemmasstuff.csv");
		// ArrayList<SearchResult> results =
		// s.search("countryCodeValue,([A-Z0-9]+)", -1);
		List<SearchResult> results = s.search("lastName,(.+)", -1);
		SearchResult.removeDuplicates(results); // doesn't work yet. Use excel
												// to remove duplicates

		for (SearchResult result : results) {
			System.out.println(result.matchedPart);
		}

	}
}
