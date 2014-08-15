package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.play.helpers.IFileWrapper;

import org.dolan.datastructures.IProcessedFile;
import org.dolan.datastructures.IThreadBlock;
import org.dolan.datastructures.ProcessedFile;
import org.dolan.merger.Merger;
import org.dolan.tools.IDebenhamsAPISearcher;
import org.dolan.tools.Logger;
import org.dolan.tools.DebenhamsAPISearcher;
import org.dolan.ziptools.ZipTool;

/**
 * The Class Helper.
 * A helper class which works closely with the Application class. Everything here should only be called from the Application class.
 * Code here is only here to make the Application class look cleaner.
 */
public class Helper {

	/**
	 * Process a single Debenhams API file.
	 * The method is asynchronous to synchronous because Play Framework has their own way to deal with asynchronous methods.
	 *
	 * @param file the file
	 * @param orderID the order id
	 * @return the processed file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static IProcessedFile processFile(IFileWrapper file, int orderID) throws IOException, InterruptedException {
		if (file == null)
			throw new FileNotFoundException("No file specified.");

		IDebenhamsAPISearcher searcher = new DebenhamsAPISearcher(file.getBufferedReader());
		Logger.log("FILE TO BE PROCESSED", file.toString());
		IProcessedFile processedFile = new ProcessedFile();

		Thread findAllBlocksThread = searcher.asyncGetThreadBlocksWhichContain(orderID, (List<IThreadBlock> blocks) -> {
			if (blocks.size() > 0) {
				Logger.log("FOUND SEARCHED DATA ADDING THEM TO LIST");
				processedFile.append(blocks);
			}
		});

		findAllBlocksThread.join();

		return processedFile;
	}

	/**
	 * Process Debenhams API files.
	 *
	 * @param files the files
	 * @param orderID the order id
	 * @return the i processed file
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static IProcessedFile processFiles(List<IFileWrapper> files, Integer orderID) throws IOException, InterruptedException {
		ArrayList<IProcessedFile> processedFiles = new ArrayList<IProcessedFile>();
		if (files.size() == 0) {
			return null;
		}
		for (IFileWrapper file : files) {
			if (file == null)
				return null;
			processedFiles.add(processFile(file, orderID));
		}

		Logger.log("MERGING FILES TOGETHER");
		IProcessedFile processedFile = Merger.merge(processedFiles);
		return processedFile;
	}

	/**
	 * Creates a physical file from a list of processed files.
	 *
	 * @param processedFiles the processed files
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static File createFile(List<IProcessedFile> processedFiles) throws IOException {
		Logger.log("AMOUNT OF FILES SELECTED TO BE ZIPPED/OR NOT", processedFiles.size());
		if (processedFiles.size() > 1) {
			return Helper.toZipFile(processedFiles);
		} else {
			return Helper.toFile(processedFiles.get(0));
		}
	}

	/**
	 * Creates a physical file from a list of processed files.
	 *
	 * @param processedFiles the processed files
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static File createFile(IProcessedFile... processedFiles) throws IOException {
		return createFile(new ArrayList<IProcessedFile>(Arrays.asList(processedFiles)));
	}

	/**
	 * Creates a physical single from a single processed file.
	 *
	 * @param processedFile the processed file
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static File toFile(IProcessedFile processedFile) throws IOException {
		File file = new File("download.txt");
		BufferedWriter bwr = new BufferedWriter(new FileWriter(file));
		for (IThreadBlock threadBlock : processedFile.getThreadBlocks()) {
			bwr.write(threadBlock.toString());
		}
		bwr.flush();
		bwr.close();
		return file;
	}

	/**
	 * Creates a physical zip file from a list of processed files.
	 *
	 * @param files the files
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static File toZipFile(List<IProcessedFile> files) throws IOException {
		List<String> fileStrings = new ArrayList<String>();
		for (IProcessedFile file : files) {
			fileStrings.add(file.toString());
		}

		ZipTool.writeZip("server-files.zip", fileStrings);
		return null;
	}
}
