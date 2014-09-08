package model.play.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.dolan.tools.LogTool;
import org.dolan.ziptools.ZipTool;

/**
 * The Class UploadedZipFile.
 * This class wraps around an uploaded zip file. It gets the buffered reader streams from a zip file.
 */
public class UploadedZipFile extends UploadedFile implements IFileWrapper {

	/**
	 * Instantiates a new uploaded zip file.
	 *
	 * @param file the file
	 * @param name the name
	 */
	public UploadedZipFile(File file, String name) {
		super(file, name);
		LogTool.traceC(this.getClass(), "This file is a ZIP file");
		if (!this.getFileType().equals("zip")) {
			throw new ClassCastException("Must create UploadedZipFile with files which end in .zip. Is this really a Zip file?");
		}
		LogTool.traceC(this.getClass(), "ZIP file uploaded", name);
	}

	/* (non-Javadoc)
	 * @see model.play.helpers.UploadedFile#getBufferedReader()
	 */
	@Override
	public List<BufferedReader> getBufferedReaders() {
		LogTool.traceC(this.getClass(), "Retrieving buffered readers from ZIP file");
		try {
			LogTool.traceC(this.getClass(), "Found buffered readers");
			return ZipTool.getBufferedReadersFromZip(this.file);
		} catch (IOException e) {
			LogTool.errorC(this.getClass(), "Something wrong with getting the buffered reader", e);
		}
		return Collections.<BufferedReader>emptyList();
	}
}
