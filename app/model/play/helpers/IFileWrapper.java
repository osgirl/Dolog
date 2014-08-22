package model.play.helpers;

import java.io.BufferedReader;
import java.util.List;

/**
 * The Interface IFileWrapper.
 * An interface which represents a file.
 */
public interface IFileWrapper {
	
	/**
	 * Gets the buffered reader.
	 *
	 * @return the buffered reader
	 */
	List<BufferedReader> getBufferedReaders();

	/**
	 * Gets the name.
	 *
	 * @return the name
	 */
	String getName();

	/**
	 * Gets the file type.
	 *
	 * @return the file type
	 */
	String getFileType();

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	String getID();

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	long getSize();

}
