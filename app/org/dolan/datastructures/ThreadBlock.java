package org.dolan.datastructures;

import java.util.ArrayList;
import java.util.List;

import org.dolan.tools.LogTool;

/**
 * The Class ThreadBlock.
 * An implementation of IThreadBlock
 * It represents a logical grouping of lines which is ran on the same thread.
 * Used in the analysis and filtering algorithm.
 */
public class ThreadBlock implements IThreadBlock {
	
	/** The lines. */
	private List<ILine> lines;
	
	/** The thread number. */
	private int threadNumber = -1;

	/**
	 * Instantiates a new thread block.
	 */
	public ThreadBlock() {
		lines = new ArrayList<ILine>();
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IThreadBlock#addLine(java.lang.String, int)
	 */
	@Override
	public boolean addLine(String lineString, int lineNumber) {
		ILine line = new Line(lineString, lineNumber);
		if (threadNumber == -1) { // No thread number set
			threadNumber = line.getThreadNumber();
		} else {
			if (line.getThreadNumber() != threadNumber) {
				return false;
			}
		}

		lines.add(line);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IThreadBlock#addLine(org.dolan.datastructures.ILine)
	 */
	@Override
	public boolean addLine(ILine line) {
		if (line == null) {
			IllegalArgumentException iae = new IllegalArgumentException("Expected line to be inserted, but got none.");
			LogTool.error("Expected line to be inserted, but got none.", iae);
		}
		lines.add(line);
		return true;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IThreadBlock#getLines()
	 */
	@Override
	public List<ILine> getLines() {
		return lines;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IThreadBlock#getSize()
	 */
	@Override
	public int getSize() {
		return this.lines.size();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ILine line : lines) {
			sb.append(line.toString() + "\r\n");
		}

		return sb.toString();
	}
}
