package org.dolan.datastructures;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.dolan.tools.IDebenhamsAPISearcher;
import org.dolan.tools.DebenhamsAPISearcher;
import org.junit.Test;

public class ProcessedFileTester {
	private static IDebenhamsAPISearcher searcher;
	
	private BufferedReader filePathtoBufferedReader(String filePath) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		return br;
	}
	
	@Test
	public void testIfProcessedFileCanIterateThrough() throws IOException {
		searcher = new DebenhamsAPISearcher(filePathtoBufferedReader("C:\\server02.log.2014-07-03"));
		IProcessedFile processedFile = new ProcessedFile();

		Thread findAllBlocksThread = searcher.asyncGetThreadBlocksWhichContain(719342972, (List<IThreadBlock> blocks) -> {
			processedFile.append(blocks);
		});

		try {
			findAllBlocksThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		for (ILine line = processedFile.getNextLine(); line != null; line = processedFile.getNextLine()) {
			System.out.println(line);
			assertEquals(line != null, true);
		}
		
		
	}
}
