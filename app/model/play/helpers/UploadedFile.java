package model.play.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

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
		this.file = file;
		this.size = file.length();
	}

	/* (non-Javadoc)
	 * @see model.play.helpers.IFileWrapper#getBufferedReader()
	 */
	@Override
	public BufferedReader getBufferedReader() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return br;
	}
}
