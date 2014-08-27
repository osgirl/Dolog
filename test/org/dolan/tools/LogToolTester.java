package org.dolan.tools;

import org.junit.Test;

public class LogToolTester {

	@Test
	public void testIfCanTraceLog() {
		LogTool.log("test");
	}
	
	@Test
	public void testIfCanLogNull() {
		LogTool.log(null);
	}
	
	@Test
	public void testIfCanLog2() {
		LogTool.log("Testing number 2", "hello");
	}
}
