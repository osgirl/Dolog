package org.dolan.datastructures;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.dolan.tools.LogTool;

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
		LogTool.traceC(this.getClass(), "Creating processed file");
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
		LogTool.traceC(this.getClass(), "Begin appending Thread Blocks to file");
		Objects.requireNonNull(tb);
		for (IThreadBlock threadBlock : tb) {
			if (threadBlock.getSize() > 0) {
				LogTool.traceC(this.getClass(), "Adding Thread Block to file", threadBlock);
				this.threadBlocks.add(threadBlock);
			} else {
				LogTool.warnC(this.getClass(), "Trying to add zero sized Thread Block to file. It has been skipped", threadBlock);
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IProcessedFile#getNextLine()
	 */
	@Override
	public ILine getNextLine() {
		LogTool.traceC(this.getClass(), "Getting next line");
		if (this.threadBlocks.size() == 0) {
			LogTool.warnC(this.getClass(), "There are no Thread Blocks in file. Thus no next line", this.threadBlocks);
			return null;
		}
		
		if (lineCount > this.threadBlocks.get(threadBlockCount).getSize() - 1) {
			threadBlockCount++;
			lineCount = 0;
		}

		if (threadBlockCount > this.threadBlocks.size() - 1) {
			LogTool.traceC(this.getClass(), "Reached to the end of the file. Thread Block counter is at: " + threadBlockCount + ", and Total Thread Blocks is: " + this.threadBlocks.size());
			return null;
		}

		ILine line = this.threadBlocks.get(threadBlockCount).getLines().get(lineCount);
		LogTool.traceC(this.getClass(), "Got next line", line);
		lineCount++;
		return line;
	}

	/* (non-Javadoc)
	 * @see org.dolan.datastructures.IProcessedFile#getLine(int)
	 */
	@Override
	public ILine getLine(int number) {
		LogTool.traceC(this.getClass(), "Getting line from line number", number);
		if (number < 0) {
			throw new IllegalArgumentException("Cant have a negative line number");
		}
		
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
		LogTool.traceC(this.getClass(), "Resetting line stream");
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
