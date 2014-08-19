package org.dolan.datastructures;

// TODO: Auto-generated Javadoc
/**
 * The Interface ILine. 
 * Thus interface is used to model a line from a Debenhams API log file.
 */
public interface ILine {
	
	/**
	 * Gets the thread number.
	 *
	 * @return the thread number
	 */
	public int getThreadNumber();
	
	/**
	 * Gets the time stamp.
	 *
	 * @return the time stamp
	 */
	public TimeStamp getTimeStamp();
}
