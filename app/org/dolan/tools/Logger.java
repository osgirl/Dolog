package org.dolan.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.dolan.datastructures.IThreadBlock;

/**
 * The Class Logger.
 * This class handles all the logging.
 * All logging in the application goes through this for more consistency.
 * This allows easy on/off of logging.
 * This class also handles writing logs and files to disk. Some are depreciated.
 */
public class Logger {

	/**
	 * Write thread blocks to file.
	 *
	 * @param filename the filename
	 * @param threadBlocks the thread blocks
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static File writeThreadBlocksToFile(String filename, List<IThreadBlock> threadBlocks) throws IOException {
		File file = null;

		file = new File(filename);

		if (!file.exists()) {
			file.createNewFile();
		}

		FileWriter fw = new FileWriter(file.getAbsoluteFile());
		BufferedWriter bw = new BufferedWriter(fw);

		for (IThreadBlock threadBlock : threadBlocks) {
			bw.write(threadBlock.toString());
		}

		bw.close();

		return file;
	}

	// TODO
	/**
	 * Write file to disk.
	 *
	 * @param file the file
	 * @param filePath the file path
	 */
	public static void writeFileToDisk(File file, String filePath) {
		try {
			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);

			bw.close();

			Logger.log("WRITTEN FILE TO DISK");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write file to temporary folder.
	 *
	 * @param file the file
	 */
	public static void writeFileToTempFolder(File file) {
		writeFileToDisk(file, "/tmp/play");
	}
	
	/**
	 * Log data to console.
	 *
	 * @param heading the heading of the log data
	 * @param data the data of the log
	 */
	public static void log(String heading, Object data) {
		System.out.println(heading.toUpperCase() + ":\n\t" + data);
	}
	
	/**
	 * Log data to console.
	 *
	 * @param heading the text to log
	 */
	public static void log(String heading) {
		System.out.println(heading.toUpperCase() + "...");
	}
	
	/**
	 * Log data to console.
	 *
	 * @param obj the object
	 */
	public static void log(Object obj) {
		System.out.println(obj);
	}
}
