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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.dolan.remoteaccess.ISFTPManager;
import org.dolan.ziptools.ZipTool;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * The Class ServerFile.
 * A file which is on a server. It is different to a traditional file because it cannot have any physical data on it upon instantiation. This is because it will use too much memory storing the entire servers worth of data physically in memory.
 * So, instead, a reference is made, and when needed, it will "materialise" the file so it contains data. But for now, it is just a reference.
 */
public class ServerFile extends BaseFile implements IFileWrapper {

	/** The br. */
	private BufferedReader br;
	private String date;

	/**
	 * Instantiates a new server file.
	 *
	 * @param name the name
	 */
	public ServerFile(String name, long size, String date) {
		super(name);
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
	 */
	private void setBufferedReader(BufferedReader br) {
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
		for (ServerFile file : serverFiles) {
			if (file.getName().equals(name)) {
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
		File file = new File("APIservertemp.zip");
		OutputStream outputStream = null;
		outputStream = new FileOutputStream(file);
		IOUtils.copy(inputStream, outputStream);
		outputStream.close();

		List<BufferedReader> readers = ZipTool.getBufferedReadersFromZip(file);
		BufferedReader reader = readers.get(0);

		if (!reader.ready()) {
			throw new IOException("BufferedReader is empty. BufferedReader needs to be non-empty to extract data.");
		}
		setBufferedReader(reader);
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

	public String getDate() {
		return this.date;
	}
}
