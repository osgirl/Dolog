package model.play.helpers;

import java.io.BufferedReader;

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
	BufferedReader getBufferedReader();

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
