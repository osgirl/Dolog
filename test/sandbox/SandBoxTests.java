package sandbox;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.dolan.datastructures.IThreadBlock;
import org.dolan.tools.IDebenhamsAPISearcher;
import org.dolan.tools.LogTool;
import org.dolan.tools.DebenhamsAPISearcher;
import org.junit.Ignore;
import org.junit.Test;

public class SandBoxTests {
	public static IDebenhamsAPISearcher searcher;

	private BufferedReader filePathtoBufferedReader(String filePath) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		return br;
	}

	@Ignore
	@Test
	public void testJiraCase() throws IOException {
		searcher = new DebenhamsAPISearcher(filePathtoBufferedReader("C:\\Users\\Dolan.Miu\\Desktop\\Jiras\\DEBAPIS-157\\RAW\\server01.log.2014-06-30"));
		ArrayList<IThreadBlock> threadBlocks = new ArrayList<IThreadBlock>();

		Thread findAllBlocksThread = searcher.asyncGetThreadBlocksWhichContain(719088772, (List<IThreadBlock> blocks) -> {
			threadBlocks.addAll(blocks);
		});

		System.out.println("testIfSearchesThreadForGivenOrderID Test Running...");

		try {
			findAllBlocksThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LogTool.writeThreadBlocksToFile("C:\\Users\\Dolan.Miu\\Desktop\\Jiras\\DEBAPIS-157\\outputServer1blah.txt", threadBlocks);
		assertEquals(12, threadBlocks.size());
	}

	@Ignore
	@Test
	public void testJiraCase2() throws IOException {
		searcher = new DebenhamsAPISearcher(filePathtoBufferedReader("C:\\Users\\Dolan.Miu\\Desktop\\ramesan\\01server.log.2014-07-16"));
		ArrayList<IThreadBlock> threadBlocks = new ArrayList<IThreadBlock>();

		Thread findAllBlocksThread = searcher.asyncGetThreadBlocksWhichContain(720426553, (List<IThreadBlock> blocks) -> {
			threadBlocks.addAll(blocks);
		});

		System.out.println("testIfSearchesThreadForGivenOrderID Test Running...");

		try {
			findAllBlocksThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		LogTool.writeThreadBlocksToFile("C:\\Users\\Dolan.Miu\\Desktop\\ramesan\\outputServer1.txt", threadBlocks);
		assertEquals(12, threadBlocks.size());
	}

	@Test
	public void testIfSearchesForSessionID() throws IOException {
		searcher = new DebenhamsAPISearcher(filePathtoBufferedReader("C:\\Users\\Dolan.Miu\\Desktop\\Jiras\\DEBAPIS-160\\721563254-server1.txt"));
		ArrayList<String> sessionIDs = new ArrayList<String>();

		Thread findAllBlocksThread = searcher.asyncGetSessionIDWhichContain(721563254, (List<String> IDs) -> {
			sessionIDs.addAll(IDs);
		});

		System.out.println("testIfSearchesForSessionID Test Running...");

		try {
			findAllBlocksThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		assertEquals(1, sessionIDs.size());
		System.out.println(sessionIDs);
		System.out.println("testIfSearchesForSessionID Test Finish...");
	}
}
