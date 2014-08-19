package org.dolan.callbacks;

import java.util.List;

import org.dolan.datastructures.IThreadBlock;

/**
 * The Interface IThreadBlockCallback.
 */
public interface IThreadBlockCallback {
	
	/**
	 * The callback function.
	 *
	 * @param threadblocks the ThreadBlocks
	 */
	void call(List<IThreadBlock> threadblocks);
}
