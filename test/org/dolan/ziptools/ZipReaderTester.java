package org.dolan.ziptools;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

public class ZipReaderTester {

	@Test
	public void testIfCanReadZipFile() throws IOException {
		List<BufferedReader> readers = ZipTool.getBufferedReadersFromZip(new File("C:\\server.log.2014-07-14.zip"));
		System.out.println(readers.size());
		assertEquals(readers.size() > 0, true);
	}

	@Test
	public void testIfCanWriteZipFile() throws IOException {
		ZipTool.writeZip("testFile.zip", "hello", "blah");
		assertEquals(new File("testFile.zip").isFile(), true);
	}
	
	@Test
	public void testIfWrittenZipFileContainsCorrectData() throws IOException {
		ZipTool.writeZip("testFile.zip", "hello", "blah");
		List<BufferedReader> readers = ZipTool.getBufferedReadersFromZip(new File("testFile.zip"));
		System.out.println(readers.size());
		assertEquals(readers.size() > 0, true);
	}
}
