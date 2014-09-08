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
import org.dolan.tools.LogTool;

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
		LogTool.trace("Begin merging files", files);
		if (files.size() < 2) {
			LogTool.trace("Nothing to merge, single file");
			return files.get(0);
		}
		
		IProcessedFile processedFile = new ProcessedFile();
		List<IThreadBlock> threadBlocks = new ArrayList<IThreadBlock>();
		IThreadBlock tb = new ThreadBlock();
		threadBlocks.add(tb);
		
		LogTool.trace("Creating priority queue to merge files");
		PriorityQueue<ILine> lineHeap = new PriorityQueue<ILine>((ILine x, ILine y) -> {
			return x.getTimeStamp().compareTo(y.getTimeStamp());
		});
		
		LogTool.trace("Going through each line and adding the next line in the priority queue");
		for (IProcessedFile file : files) {
			file.resetStream();
			for (ILine line = file.getNextLine(); line != null; line = file.getNextLine()) {
				lineHeap.add(line);
			}
		}
		
		LogTool.trace("Going through each line and throwing out the next line in the priority queue and adding to list");
		while (!lineHeap.isEmpty()) {
			tb.addLine(lineHeap.remove());
		}
		processedFile.append(threadBlocks);
		LogTool.trace("Finish merging files", files);
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
