package org.dolan.ziptools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.dolan.tools.Logger;

/**
 * The Class ZipTool.
 * This class handles all the zipping and reading of zip files.
 */
public class ZipTool {
	
	/** The current open zip files. */
	private static Stack<ZipFile> openZipFiles = new Stack<ZipFile>();

	/**
	 * Gets the buffered readers from zip.
	 *
	 * @param file the file
	 * @return the buffered readers from zip
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@SuppressWarnings("resource")
	public static List<BufferedReader> getBufferedReadersFromZip(File file) throws IOException {
		List<BufferedReader> bufferedReaders = new ArrayList<BufferedReader>();
		ZipFile zf = new ZipFile(file);
		Enumeration<? extends ZipEntry> entries = zf.entries();

		while (entries.hasMoreElements()) {
			ZipEntry ze = (ZipEntry) entries.nextElement();

			long size = ze.getSize();
			if (size > 0) {
				Logger.log("NAME AND SIZE OF ZIP FILE ENTRY", ze.getName() + ", " + size);
				BufferedReader br = new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
				bufferedReaders.add(br);
			} else {
				Logger.log("FILE IN ZIP IS SIZE ZERO. WON'T BE RETURNING READER", ze.getName());
			}
		}

		if (bufferedReaders.size() == 0) {
			throw new NullPointerException("Cannot load files. No files in zip file");
		}

		checkIfReadersAreEmpty(bufferedReaders);
		return bufferedReaders;
	}

	/**
	 * Write zip file.
	 *
	 * @param fileName the file name
	 * @param data the data
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static File writeZip(String fileName, String... data) throws IOException {
		return writeZip(fileName, Arrays.asList(data));
	}
	
	/**
	 * Write zip file.
	 *
	 * @param fileName the file name
	 * @param data the data
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static File writeZip(String fileName, List<String> data) throws IOException {
		Logger.log("CREATING ZIP FILE", fileName);
		Map<String, String> entries = new HashMap<String, String>();
		for (int i = 0; i < data.size(); i++) {
			entries.put(data.get(i), "output (" + i + ").txt");
		}

		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		File outputZip = new File(fileName);
		fos = new FileOutputStream(outputZip);

		zos = new ZipOutputStream(fos);

		for (Map.Entry<String, String> mapEntry : entries.entrySet()) {
			// create a new zip file entry with name, e.g. "1.txt"
			ZipEntry entry = new ZipEntry(mapEntry.getValue());
			// set the compression method
			entry.setMethod(ZipEntry.DEFLATED);
			// add the ZipEntry to the ZipOutputStream
			zos.putNextEntry(entry);
			// write the ZipEntry content
			zos.write(mapEntry.getKey().getBytes());
		}

		if (zos != null) {
			zos.close();
		}

		return outputZip;
	}

	/**
	 * Check if readers are empty.
	 *
	 * @param bufferedReaders the buffered readers
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static void checkIfReadersAreEmpty(List<BufferedReader> bufferedReaders) throws IOException {
		boolean error = false;
		List<Integer> badReaders = new ArrayList<Integer>();

		for (int i = 0; i < bufferedReaders.size(); i++) {
			if (!bufferedReaders.get(i).ready()) {
				badReaders.add(i);
				error = true;
			}
		}

		if (error) {
			throw new IOException("Readers: " + badReaders.toString() + " is empty, there is something wrong with loading the BufferedReader from the Zip file.");
		}
	}
	
	/**
	 * Close all zip files.
	 *
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static boolean closeAllZipFiles() throws IOException {
		if (openZipFiles.empty()) return false;
		
		while (!openZipFiles.empty()) {
			openZipFiles.pop().close();
		}
		return true;
	}
}
