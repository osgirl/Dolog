package org.dolan.datastructures;

import java.util.List;

/**
 * The Interface IProcessedFile. 
 * IProcessedFile is the file which is returned after being processed.
 * It contains many (or one) ThreadBlock which represents the file.
 */
public interface IProcessedFile {
	
	/**
	 * Gets the thread blocks.
	 *
	 * @return the thread blocks
	 */
	public List<IThreadBlock> getThreadBlocks();

	/**
	 * Add a IThreadBlock into the processed file.
	 *
	 * @param tb the List of ThreadBlocks
	 */
	public void append(List<IThreadBlock> tb);

	/**
	 * Gets the next line of the file.
	 *
	 * @return the next line
	 */
	public ILine getNextLine();

	/**
	 * Gets a line based on the line number.
	 *
	 * @param number the line number
	 * @return the line
	 */
	public ILine getLine(int number);

	/**
	 * Reset the stream so it points to the head again, so you can getNextLine() again.
	 */
	public void resetStream();
}
