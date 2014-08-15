package org.dolan.remoteaccess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;

import model.play.helpers.ServerFile;

import org.junit.Before;
import org.junit.Test;

import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

public class SFTPManagerTester {
	ISFTPManager connector;

	@Before
	public void setUp() {
		connector = new SFTPManager("eap6", "176.74.183.138", 22, "prodapi", "/opt/jboss-eap-6.0/domain/servers/api02/log");
	}

	@Test
	public void testIfCanConnect() {
		try {
			connector.connect();
		} catch (JSchException e) {
			assertFalse(true);
		}
		connector.disconnect();
		assertTrue(true);
	}

	@Test
	public void testIfCanGetFiles() throws JSchException, SftpException {
		List<ServerFile> sFiles = connector.getFiles("zip");
		assertEquals(sFiles.size() > 5, true);
		/*
		 * for (ServerFile sFile : sFiles) { connector.materialse(sFile); }
		 */
	}

	
	public void testIfCanMaterialiseFile() throws JSchException, SftpException, IOException {
		List<ServerFile> sFiles = connector.getFiles("zip");
		sFiles.get(0).materialise(connector);
		assertEquals(sFiles.get(0).getSize() > 0, true);
	}
	
	@Test
	public void testIfCanSFTPConnect() {
		try {
			connector.connect();
			connector.sftpConnect();
			connector.disconnect();
		} catch (JSchException e) {
			assertFalse(true);
		}
		assertTrue(true);
	}
}
