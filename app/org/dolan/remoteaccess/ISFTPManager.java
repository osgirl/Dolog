package org.dolan.remoteaccess;

import java.util.List;

import model.play.helpers.ServerFile;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * The Interface ISFTPManager.
 * This is a wrapper of JSch to handle all the server interactions
 */
public interface ISFTPManager {
	
	/**
	 * Connect to the server.
	 *
	 * @throws JSchException the j sch exception
	 */
	public void connect() throws JSchException;
	
	/**
	 * Disconnect the server.
	 */
	public void disconnect();
	
	/**
	 * Connect via SFTP.
	 *
	 * @return the SFTP channel
	 * @throws JSchException the JSch exception
	 */
	public ChannelSftp sftpConnect() throws JSchException;
	
	/**
	 * Gets the files from the server.
	 *
	 * @param filterFileType the filter file type
	 * @return the files
	 * @throws JSchException the JSch exception
	 * @throws SftpException the SFTP exception
	 */
	public List<ServerFile> getFiles(String filterFileType) throws JSchException, SftpException;

	/**
	 * Gets the path of the server currently.
	 *
	 * @return the path
	 */
	public String getPath();
}
