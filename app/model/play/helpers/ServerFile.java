/*
 * 
 */
package model.play.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.dolan.remoteaccess.ISFTPManager;
import org.dolan.tools.LogTool;
import org.dolan.ziptools.ZipTool;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

// TODO: Auto-generated Javadoc
/**
 * The Class ServerFile.
 * A file which is on a server. It is different to a traditional file because it cannot have any physical data on it upon instantiation. This is because it will use too much memory storing the entire servers worth of data physically in memory.
 * So, instead, a reference is made, and when needed, it will "materialise" the file so it contains data. But for now, it is just a reference.
 */
public class ServerFile extends BaseFile implements IFileWrapper {

	/** The buffered reader. */
	private BufferedReader br;
	
	/** The date. */
	private String date;

	/**
	 * Instantiates a new server file.
	 *
	 * @param name the name
	 * @param size the size
	 * @param date the date
	 */
	public ServerFile(String name, long size, String date) {
		super(name);
		Objects.requireNonNull(date, "Date cannot be null");
		if (size <= 0) {
			throw new IllegalArgumentException("File size cannot be less than or equal to zero");
		}
		
		if (date.isEmpty()) {
			throw new IllegalArgumentException("Date cannot be blank");
		}
		LogTool.traceC(this.getClass(), "File is infact a server file with size", size);
		LogTool.traceC(this.getClass(), "And with creation date of", date);
		this.size = size;
		this.date = date;
	}

	/* (non-Javadoc)
	 * @see model.play.helpers.IFileWrapper#getBufferedReader()
	 */
	@Override
	public List<BufferedReader> getBufferedReaders() {
		return Arrays.asList(this.br);
	}

	/**
	 * Sets the buffered reader.
	 *
	 * @param br the new buffered reader
	 * @throws IOException 
	 */
	private void setBufferedReader(BufferedReader br) throws IOException {
		Objects.requireNonNull(br);
		if (!br.ready()) {
			throw new IOException("BufferedReader is empty. BufferedReader needs to be non-empty to extract data.");
		}
		this.br = br;
	}

	/**
	 * Find server file from name.
	 *
	 * @param serverFiles the server files
	 * @param name the name
	 * @return the server file
	 * @throws FileNotFoundException the file not found exception
	 */
	public static ServerFile findServerFileFromName(List<ServerFile> serverFiles, String name) throws FileNotFoundException {
		LogTool.trace("Finding file", name);
		for (ServerFile file : serverFiles) {
			LogTool.trace("Is it " + file.getName() + "?");
			if (file.getName().equals(name)) {
				LogTool.trace("Yes it is. Returning");
				return file;
			}
		}
		throw new FileNotFoundException("Cannot find file! Something is wrong");
	}

	/**
	 * Gets data from the server.
	 *
	 * @param inputStream the input stream
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void materialise(InputStream inputStream) throws IOException {
		LogTool.traceC(this.getClass(), "Begin materialising file");
		String fileName = "APIservertemp.zip";
		LogTool.traceC(this.getClass(), "Creating file on disk", fileName);
		File file = new File(fileName);
		OutputStream outputStream = new FileOutputStream(file);
		LogTool.traceC(this.getClass(), "Copying file from server to disk. This is probably really bad for simutaneous users");
		IOUtils.copy(inputStream, outputStream);
		outputStream.close();

		List<BufferedReader> readers = ZipTool.getBufferedReadersFromZip(file);
		if (readers.size() > 1) {
			LogTool.warnC(this.getClass(), "There is more readers in ZIP file. Server acrhitecture changed? Amount of readers", readers.size());
		}
		
		LogTool.traceC(this.getClass(), "Obtaining first buffered reader");
		BufferedReader reader = readers.get(0);

		setBufferedReader(reader);
		LogTool.trace("Finish materialising file");
	}

	/**
	 *  Gets data from the server.
	 *
	 * @param sftpManager the sftp manager
	 * @throws JSchException the j sch exception
	 * @throws SftpException the sftp exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void materialise(ISFTPManager sftpManager) throws JSchException, SftpException, IOException {
		sftpManager.connect();
		sftpManager.getPath();
		ChannelSftp sftpChannel = sftpManager.sftpConnect();
		InputStream inputStream = sftpChannel.get(sftpManager.getPath() + this.getName());
		materialise(inputStream);
		sftpManager.disconnect();
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return this.date;
	}
}
