package org.dolan.tools;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.play.helpers.IFileWrapper;
import model.play.helpers.ServerFile;

import org.dolan.datastructures.IProcessedFile;
import org.dolan.datastructures.IThreadBlock;
import org.dolan.merger.Merger;
import org.dolan.remoteaccess.ISFTPManager;
import org.dolan.remoteaccess.SFTPManager;
import org.dolan.ziptools.ZipTool;
import org.junit.Test;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

import controllers.Helper;

public class ThreadSearcherTester {

	public static IDebenhamsAPISearcher searcher;

	private BufferedReader filePathtoBufferedReader(String filePath) throws FileNotFoundException {
		BufferedReader br = new BufferedReader(new FileReader(filePath));
		return br;
	}

	@Test
	public void testIfSearchesThreadForGivenOrderID() throws IOException, InterruptedException {
		searcher = new DebenhamsAPISearcher(filePathtoBufferedReader("C:\\server02.log.2014-07-03"));
		List<IThreadBlock> threadBlocks = new ArrayList<IThreadBlock>();

		Thread findAllBlocksThread = searcher.asyncGetThreadBlocksWhichContain(719342972, (List<IThreadBlock> blocks) -> {
			threadBlocks.addAll(blocks);
		});

		System.out.println("testIfSearchesThreadForGivenOrderID Test Running...");

		findAllBlocksThread.join();

		Logger.writeThreadBlocksToFile("C:\\Users\\Dolan.Miu\\Desktop\\outputStuff.txt", threadBlocks);
		assertEquals(12, threadBlocks.size());

		System.out.println("testIfSearchesThreadForGivenOrderID Test Finish...");
	}

	@Test
	public void testIfSearchesForSessionID() throws IOException, InterruptedException {
		searcher = new DebenhamsAPISearcher(filePathtoBufferedReader("C:\\server02.log.2014-07-03"));
		List<String> sessionIDs = new ArrayList<String>();

		Thread findAllBlocksThread = searcher.asyncGetSessionIDWhichContain(719342972, (List<String> IDs) -> {
			sessionIDs.addAll(IDs);
		});

		System.out.println("testIfSearchesForSessionID Test Running...");

		findAllBlocksThread.join();

		assertEquals(1, sessionIDs.size());

		System.out.println("testIfSearchesForSessionID Test Finish...");
	}

	@Test
	public void testIfSearchesJiraForSessionID() throws IOException, InterruptedException {
		searcher = new DebenhamsAPISearcher(filePathtoBufferedReader("C:\\Users\\Dolan.Miu\\Desktop\\Jiras\\DEBAPIS-157\\RAW\\server01.log.2014-06-30"));
		List<String> sessionIDs = new ArrayList<String>();

		Thread findAllBlocksThread = searcher.asyncGetSessionIDWhichContain(719088772, (List<String> IDs) -> {
			sessionIDs.addAll(IDs);
		});

		System.out.println("testIfSearchesForSessionID Test Running...");

		findAllBlocksThread.join();

		assertEquals(1, sessionIDs.size());

		System.out.println("testIfSearchesForSessionID Test Finish...");
	}

	@Test
	public void testIfCanSearchFromAPIServer() throws JSchException, SftpException, IOException, InterruptedException {
		ISFTPManager sftpDebAPI1 = new SFTPManager("eap6", "176.74.183.149", 22, "prodapi", "/opt/jboss-eap-6.0/domain/servers/api01/log/");
		ISFTPManager sftpDebAPI2 = new SFTPManager("eap6", "176.74.183.138", 22, "prodapi", "/opt/jboss-eap-6.0/domain/servers/api02/log/");

		String fileIDStrings = "server.log.2014-08-12.zip";
		Integer orderID = 634563;
		String[] fileIDs = fileIDStrings.split(",");
		List<ServerFile> sFiles1 = sftpDebAPI1.getFiles("zip");
		List<ServerFile> sFiles2 = sftpDebAPI2.getFiles("zip");
		List<IProcessedFile> processedFiles = new ArrayList<IProcessedFile>();

		for (String fileID : fileIDs) {
			ServerFile file1 = ServerFile.findServerFileFromName(sFiles1, fileID);
			file1.materialise(sftpDebAPI1);
			IProcessedFile pfile1 = Helper.processFile(file1, orderID);

			ServerFile file2 = ServerFile.findServerFileFromName(sFiles2, fileID);
			file2.materialise(sftpDebAPI2);
			IProcessedFile pfile2 = Helper.processFile(file2, orderID);

			IProcessedFile pfile = Merger.merge(pfile1, pfile2);
			processedFiles.add(pfile);
		}

		ZipTool.closeAllZipFiles();
		assertEquals(processedFiles.size() > 0, true);
	}

	@Test
	public void testIfCanSearchFromSingleAPIServer() throws IOException, JSchException, SftpException, InterruptedException {
		SFTPManager sftpDebAPI1 = new SFTPManager("eap6", "176.74.183.149", 22, "prodapi", "/opt/jboss-eap-6.0/domain/servers/api01/log/");

		String fileID = "server.log.2014-08-12.zip";
		Integer orderID = 634563;
		List<ServerFile> sFiles1 = sftpDebAPI1.getFiles("zip");

		ServerFile file1 = ServerFile.findServerFileFromName(sFiles1, fileID);
		file1.materialise(sftpDebAPI1);

		List<IFileWrapper> files = new ArrayList<IFileWrapper>();
		files.add(file1);

		IProcessedFile file = Helper.processFiles(files, orderID);
		assertEquals(file.getLine(0) != null, true);
	}
}
