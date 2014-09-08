package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.play.helpers.IFileWrapper;

import org.dolan.datastructures.IProcessedFile;
import org.dolan.datastructures.IThreadBlock;
import org.dolan.datastructures.ProcessedFile;
import org.dolan.merger.Merger;
import org.dolan.tools.DebenhamsAPISearcher;
import org.dolan.tools.IDebenhamsAPISearcher;
import org.dolan.tools.LogTool;

public class DebenhamsAPIHelper {

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
		Objects.requireNonNull(file, "No file specified");
		LogTool.trace("Begin processing file", file);
		
		LogTool.log("Uploaded file to be processed", file.getName());
		ArrayList<IProcessedFile> processedFiles = new ArrayList<IProcessedFile>();
		
		for(BufferedReader reader : file.getBufferedReaders()) {
			processedFiles.add(searchBufferedReader(reader, orderID));
		}

		LogTool.trace("Merging files in same ZIP together");
		IProcessedFile processedFile = Merger.merge(processedFiles);
		LogTool.trace("Finish processing file");
		return processedFile;
	}
	
	private static IProcessedFile searchBufferedReader(BufferedReader reader, int orderID) throws IOException, InterruptedException {
		LogTool.trace("Searching buffered reader for orderID", orderID);
		IDebenhamsAPISearcher searcher = new DebenhamsAPISearcher(reader);
		IProcessedFile processedFile = new ProcessedFile();

		Thread findAllBlocksThread = searcher.asyncGetThreadBlocksWhichContain(orderID, (List<IThreadBlock> blocks) -> {
			if (blocks.size() > 0) {
				LogTool.trace("Found search data and adding to list", blocks);
				processedFile.append(blocks);
			}
		});

		findAllBlocksThread.join();
		LogTool.trace("Finished searching current buffered reader");
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
		if (files.isEmpty()) {
			LogTool.trace("No files passed");
			return null;
		}
		LogTool.trace("Processing files", files);
		List<IProcessedFile> processedFiles = new ArrayList<IProcessedFile>();
		
		for (IFileWrapper file : files) {
			if (file == null) {
				IllegalArgumentException iae = new IllegalArgumentException("One of the files doesn't exist for some reason.");
				LogTool.error("One of the files doesn't exist for some reason.", iae);
				throw iae;
			}
			processedFiles.add(processFile(file, orderID));
		}

		LogTool.trace("Merging files together");
		IProcessedFile processedFile = Merger.merge(processedFiles);
		return processedFile;
	}
}
