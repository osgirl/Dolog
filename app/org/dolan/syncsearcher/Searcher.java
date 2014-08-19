package org.dolan.syncsearcher;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.dolan.searcher.SearchResult;

public class Searcher {
	BufferedReader br;

	public void load(String filePath) {
		try {
			br = new BufferedReader(new FileReader(filePath));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void load(BufferedReader br) {
		this.br = br;
	}

	public List<SearchResult> search(String pattern, int amountOfResults) {
		List<SearchResult> results = new ArrayList<SearchResult>();
		String line = null;
		int count = 0;
		int lineNumber = 1;
		try {
			while (count != amountOfResults) {
				line = br.readLine();
				if (line == null)
					break;

				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(line);

				if (m.find()) {
					String matchedPart = null;
					if (m.groupCount() < 1) {
						matchedPart = m.group();
					} else {
						matchedPart = m.group(1);
					}
					results.add(new SearchResult(line, matchedPart, lineNumber));
					count++;
				}
				lineNumber++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
}
