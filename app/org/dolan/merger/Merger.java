package org.dolan.merger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

import org.dolan.datastructures.ILine;
import org.dolan.datastructures.IProcessedFile;
import org.dolan.datastructures.IThreadBlock;
import org.dolan.datastructures.ProcessedFile;
import org.dolan.datastructures.ThreadBlock;
import org.dolan.tools.Logger;

/**
 * The Class Merger.
 * It merges two or more IProcessedFiles into a single one.
 */
public class Merger {
	
	/**
	 * Merge the processed files together.
	 *
	 * @param files the files
	 * @return the processed file
	 */
	public static IProcessedFile merge(List<IProcessedFile> files) {
		if (files.size() < 2) {
			Logger.log("NOTHING TO MERGE, SINGLE FILE");
			return files.get(0);
		}
		
		IProcessedFile processedFile = new ProcessedFile();
		List<IThreadBlock> threadBlocks = new ArrayList<IThreadBlock>();
		IThreadBlock tb = new ThreadBlock();
		threadBlocks.add(tb);
		
		PriorityQueue<ILine> lineHeap = new PriorityQueue<ILine>((ILine x, ILine y) -> {
			return x.getTimeStamp().compareTo(y.getTimeStamp());
		});
		
		for (IProcessedFile file : files) {
			file.resetStream();
			for (ILine line = file.getNextLine(); line != null; line = file.getNextLine()) {
				lineHeap.add(line);
			}
		}
		
		while (!lineHeap.isEmpty()) {
			tb.addLine(lineHeap.remove());
		}
		processedFile.append(threadBlocks);
		return processedFile;
	}
	
	/**
	 * Merge the processed files together.
	 *
	 * @param files the files
	 * @return the processed file
	 */
	public static IProcessedFile merge(IProcessedFile...files) {
		return merge(new ArrayList<IProcessedFile>(Arrays.asList(files)));
	}

}
