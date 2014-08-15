package org.dolan.searcher;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.dolan.searcher.SearchCache;
import org.junit.Before;
import org.junit.Test;

public class SearchCacheTester {

	private SearchCache searchCache;

	@Before
	public void setUpSearchQueue() {
	}

	@Test
	public void TestIfSearchCacheCanHold10Elements() {
		searchCache = new SearchCache();
		searchCache.setCapacity(10);
		searchCache.add("one");
		searchCache.add("two");
		searchCache.add("three");
		searchCache.add("four");
		searchCache.add("five");
		searchCache.add("six");
		searchCache.add("seven");
		searchCache.add("eight");
		searchCache.add("nine");
		searchCache.add("ten");
		String output = searchCache.poll();

		assertEquals("one", output);

	}

	@Test
	public void TestIfSearchCachePushesElementOut() {
		searchCache = new SearchCache();
		searchCache.setCapacity(9);
		searchCache.add("one");
		searchCache.add("two");
		searchCache.add("three");
		searchCache.add("four");
		searchCache.add("five");
		searchCache.add("six");
		searchCache.add("seven");
		searchCache.add("eight");
		searchCache.add("nine");
		searchCache.add("ten");
		String output = searchCache.poll();

		assertEquals("two", output);
	}

	@Test
	public void TestIfSearchCacheCanIterate() {
		int count = 0;

		searchCache = new SearchCache();
		searchCache.setCapacity(10);
		searchCache.add("one");
		searchCache.add("two");
		searchCache.add("three");
		searchCache.add("four");
		searchCache.add("five");
		searchCache.add("six");
		searchCache.add("seven");
		searchCache.add("eight");
		searchCache.add("nine");
		searchCache.add("ten");

		Iterator<String> iterator = searchCache.iterator();
		while (iterator.hasNext()) {
			System.out.println(iterator.next());
			count++;
		}
		assertEquals(10, count);
	}
}
