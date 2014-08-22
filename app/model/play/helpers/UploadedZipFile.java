package model.play.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.dolan.tools.Logger;
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
		Logger.log("ZIP FILE UPLOADED", name);
		if (!this.getFileType().equals("zip")) {
			throw new ClassCastException("Must create UploadedZipFile with files which end in .zip. Is this really a Zip file?");
		}
	}

	/* (non-Javadoc)
	 * @see model.play.helpers.UploadedFile#getBufferedReader()
	 */
	@Override
	public List<BufferedReader> getBufferedReaders() {
		try {
			return ZipTool.getBufferedReadersFromZip(this.file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
