package model.play.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.dolan.tools.LogTool;

/**
 * The Class UploadedFile.
 * A file which is uploaded onto the application. This is a physical file and will have data, and stored in the Play Framework cache.
 */
public class UploadedFile extends BaseFile implements IFileWrapper {

	/** The file. */
	protected File file;

	/**
	 * Instantiates a new uploaded file.
	 *
	 * @param file the file
	 * @param name the name
	 */
	public UploadedFile(File file, String name) {
		super(name);
		LogTool.traceC(this.getClass(), "This file is an uploaded file");
		Objects.requireNonNull(file);
		this.file = file;
		this.size = file.length();
	}

	/* (non-Javadoc)
	 * @see model.play.helpers.IFileWrapper#getBufferedReader()
	 */
	@Override
	public List<BufferedReader> getBufferedReaders() {
		BufferedReader br = null;
		LogTool.traceC(this.getClass(), "Retreiving buffered readers from file");
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			LogTool.errorC(this.getClass(), "Could not get the physical file from the disk", e);
			return Collections.<BufferedReader>emptyList();
		}
		return Arrays.asList(br);
	}
}
