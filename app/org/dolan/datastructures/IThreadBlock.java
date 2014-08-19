package org.dolan.datastructures;

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface IThreadBlock. 
 * This interface is used to model the ThreadBlock in a Debenhams API file.
 * A ThreadBlock is a collection of lines which all have something common: they have the same thread number. Aka, ran on the same thread.
 * This logical grouping of lines is critical in the analysis and filtering algorithm.
 */
public interface IThreadBlock {
	
	/**
	 * Adds a line given a string and line number.
	 *
	 * @param lineString the line string
	 * @param lineNumber the line number
	 * @return true, if successful
	 */
	public boolean addLine(String lineString, int lineNumber);

	/**
	 * Adds a line given an ILine object.
	 *
	 * @param line the line
	 * @return true, if successful
	 */
	public boolean addLine(ILine line);

	/**
	 * Gets the lines.
	 *
	 * @return the lines
	 */
	public List<ILine> getLines();

	/**
	 * Gets the size.
	 *
	 * @return the size
	 */
	public int getSize();
}
