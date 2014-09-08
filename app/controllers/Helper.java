package controllers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dolan.datastructures.IProcessedFile;
import org.dolan.datastructures.IThreadBlock;
import org.dolan.tools.LogTool;
import org.dolan.ziptools.ZipTool;

/**
 * The Class Helper.
 * A helper class which works closely with the Application class. Everything here should only be called from the Application class.
 * Code here is only here to make the Application class look cleaner.
 */
public class Helper {

	/**
	 * Creates a physical file from a list of processed files.
	 *
	 * @param processedFiles the processed files
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static File createFile(List<IProcessedFile> processedFiles) throws IOException {
		LogTool.log("Creating file from processed files");
		LogTool.trace("Processed files", processedFiles);
		if (processedFiles.size() == 0) {
			return null;
		}
		
		LogTool.log("AMOUNT OF FILES SELECTED (TO BE ZIPPED/OR NOT)", processedFiles.size());
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
		String fileName = "download.txt";
		LogTool.trace("Creating file", fileName);
		File file = new File(fileName);
		BufferedWriter bwr = new BufferedWriter(new FileWriter(file));
		for (IThreadBlock threadBlock : processedFile.getThreadBlocks()) {
			String threadBlockString = threadBlock.toString();
			LogTool.trace("Writing threadBlock to file", threadBlockString);
			bwr.write(threadBlockString);
		}
		bwr.flush();
		bwr.close();
		LogTool.trace("File created", file);
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
		LogTool.trace("Creating zip file from", files);
		List<String> fileStrings = new ArrayList<String>();
		for (IProcessedFile file : files) {
			LogTool.trace("Adding file to zip", file);
			fileStrings.add(file.toString());
		}
		File file = ZipTool.writeZip("server-files.zip", fileStrings);
		LogTool.trace("Created ZIP file");
		return file;
	}
}
