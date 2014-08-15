package org.dolan.searcher;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

public class SearcherTester {

	public static ISearcher searcher;

	@BeforeClass
	public static void setUpSearcher() {
		searcher = new Searcher(200);
		searcher.setFile("C:\\log.txt");
	}

	@Test
	public void testIfSearchWorks() {
		/*
		 * searcher.search("hello world", new Callback() { public void
		 * call(String file, int lineNumber, String result) {
		 * assertEquals("hello world", result); } });
		 */
	}

	@Test
	public void testIfSeachCanScanUp() throws InterruptedException {
		ISearcher searcher = new Searcher(200);
		searcher.setFile("C:\\log.txt");
		String[] results = new String[2];

		Thread searchDownThread = searcher.scanDown("Confirm", 1, (SearchResult result) -> {
			results[0] = result.fullLine;
		});

		searchDownThread.join();

		Thread searchUpThread = searcher.scanUp("ResultCode", 1, (SearchResult result) -> {
			results[1] = result.fullLine;
		});

		searchUpThread.join();
		searchDownThread.join();
		assertArrayEquals(new String[] { "Confirm", "ResultCode" }, results);
	}

	@Test
	public void testIfSeachCanScanDown() throws InterruptedException {
		ISearcher searcher = new Searcher(200);
		searcher.setFile("C:\\log.txt");
		String[] results = new String[1];

		Thread scanDownThread = searcher.scanDown("Confirm", 1, (SearchResult result) -> {
			results[0] = result.fullLine;
		});

		scanDownThread.join();

		assertEquals("Confirm", results[0]);
	}

	@Test
	public void testIfCanDoFullTextSearch() throws InterruptedException {
		ISearcher searcher = new Searcher(200);
		searcher.setFile("C:\\log.txt");

		List<Integer> results = new ArrayList<Integer>();

		Thread scanDownThread = searcher.scanDown("OS Information", -1, (SearchResult result) -> {
			results.add(result.lineNumber);
			System.out.println(result + ", at line: " + result.lineNumber);
		});

		scanDownThread.join();

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(6);
		expected.add(7);
		expected.add(8);
		expected.add(9);
		expected.add(10);
		expected.add(11);
		expected.add(12);
		expected.add(13);
		expected.add(14);
		expected.add(15);
		expected.add(16);
		expected.add(17);
		expected.add(18);
		expected.add(19);
		expected.add(20);
		expected.add(21);
		assertEquals(expected, results);
	}
	
	@Test
	public void testIfCanStoreSearchResults() throws InterruptedException {
		ISearcher searcher = new Searcher(200);
		searcher.setFile("C:\\log.txt");

		List<SearchResult> results = new ArrayList<SearchResult>();

		Thread scanDownThread = searcher.scanDown("OS Information", -1, (SearchResult result) -> {
			results.add(result);
			System.out.println(result + ", at line: " + result.lineNumber);
		});

		scanDownThread.join();

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(6);
		expected.add(7);
		expected.add(8);
		expected.add(9);
		expected.add(10);
		expected.add(11);
		expected.add(12);
		expected.add(13);
		expected.add(14);
		expected.add(15);
		expected.add(16);
		expected.add(17);
		expected.add(18);
		expected.add(19);
		expected.add(20);
		expected.add(21);
		assertEquals(expected, results);
	}

	@Test
	public void testIfCanReturn3ResultsFromSearch() throws InterruptedException {
		ISearcher searcher = new Searcher(200);
		searcher.setFile("C:\\log.txt");

		List<Integer> results = new ArrayList<Integer>();

		Thread scanDownThread = null;
		scanDownThread = searcher.scanDown("OS Information", 3, (SearchResult result) -> {
			results.add(result.lineNumber);
			System.out.println(result + ", at line: " + result.lineNumber);
		});

		scanDownThread.join();

		List<Integer> expected = new ArrayList<Integer>();
		expected.add(6);
		expected.add(7);
		expected.add(8);

		assertEquals(expected, results);
	}
}
