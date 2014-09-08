package org.dolan.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
		LogTool.traceC(this.getClass(), "Creating threadblock");
		lines = new ArrayList<ILine>();
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IThreadBlock#addLine(java.lang.String, int)
	 */
	@Override
	public boolean addLine(String lineString, int lineNumber) {
		LogTool.traceC(this.getClass(), "Begin adding line");
		Objects.requireNonNull(lineString, "Line string is null");
		if (lineNumber < 0) {
			throw new IllegalArgumentException("Line number cannot be negative");
		}
		
		ILine line = new Line(lineString, lineNumber);
		if (threadNumber == -1) { // No thread number set
			LogTool.traceC(this.getClass(), "No thread number set. Setting thread number for thread block");
			threadNumber = line.getThreadNumber();
		} else {
			LogTool.traceC(this.getClass(), "Thread number set. Checking if line is of same thread number as the thread block");
			if (line.getThreadNumber() != threadNumber) {
				LogTool.traceC(this.getClass(), "It is a different thread number. Didn't add line. Returning.");
				return false;
			}
		}
		LogTool.traceC(this.getClass(), "Adding line to thread block");
		lines.add(line);
		LogTool.traceC(this.getClass(), "Finish add line");
		return true;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IThreadBlock#addLine(org.dolan.datastructures.ILine)
	 */
	@Override
	public boolean addLine(ILine line) {
		LogTool.traceC(this.getClass(), "Begin adding line");
		Objects.requireNonNull(line, "Trying to add a null ILine object");
		lines.add(line);
		LogTool.traceC(this.getClass(), "Finish adding line");
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
