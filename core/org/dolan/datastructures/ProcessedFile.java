package org.dolan.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * The Class ProcessedFile.
 * An implementation of IProcessedFile.
 * It models a file which is processed.
 */
public class ProcessedFile implements IProcessedFile {
	
	/** The thread blocks. */
	private List<IThreadBlock> threadBlocks;
	
	/** The current line. Used for line stream */
	private int lineCount;
	
	/** The current thread block. Used for line stream */
	private int threadBlockCount;

	/**
	 * Instantiates a new processed file.
	 */
	public ProcessedFile() {
		threadBlocks = new ArrayList<IThreadBlock>();
		lineCount = 0;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IProcessedFile#getThreadBlocks()
	 */
	@Override
	public List<IThreadBlock> getThreadBlocks() {
		return this.threadBlocks;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IProcessedFile#append(java.util.List)
	 */
	@Override
	public void append(List<IThreadBlock> tb) {
		for (IThreadBlock threadBlock : tb) {
			if (threadBlock.getSize() > 0) 
				this.threadBlocks.add(threadBlock);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IProcessedFile#getNextLine()
	 */
	@Override
	public ILine getNextLine() {
		if (this.threadBlocks.size() == 0) {
			return null;
		}
		
		if (lineCount > this.threadBlocks.get(threadBlockCount).getSize() - 1) {
			threadBlockCount++;
			lineCount = 0;
		}

		if (threadBlockCount > this.threadBlocks.size() - 1) {
			return null;
		}

		ILine line = this.threadBlocks.get(threadBlockCount).getLines().get(lineCount);
		lineCount++;
		return line;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IProcessedFile#getLine(int)
	 */
	@Override
	public ILine getLine(int number) {
		IThreadBlock currentThreadBlock = null;
		int currentSum = 0;
		for (IThreadBlock block : this.threadBlocks) {
			currentSum += block.getSize();
			if (number < currentSum) {
				currentThreadBlock = block;
				break;
			}
		}

		int offset = currentSum - number;
		return currentThreadBlock.getLines().get(offset);
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IProcessedFile#resetStream()
	 */
	@Override
	public void resetStream() {
		this.lineCount = 0;
		this.threadBlockCount = 0;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (IThreadBlock threadBlock : this.threadBlocks) {
			sb.append(threadBlock.toString());
		}
		return sb.toString();
	}
}
