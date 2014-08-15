package controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import model.play.helpers.IFileWrapper;
import model.play.helpers.JSONHelper;
import model.play.helpers.ServerFile;
import model.play.helpers.SessionManager;
import model.play.helpers.UploadedFile;

import org.dolan.datastructures.IProcessedFile;
import org.dolan.merger.Merger;
import org.dolan.remoteaccess.ISFTPManager;
import org.dolan.remoteaccess.SFTPManager;
import org.dolan.searcher.ISearcher;
import org.dolan.searcher.SearchResult;
import org.dolan.searcher.Searcher;
import org.dolan.tools.Logger;
import org.dolan.ziptools.ZipTool;

import play.Routes;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import views.html.index;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;

/**
 * The Class Application.
 * This is the main entry point for the application. All HTTP routes start at this.
 */
public class Application extends Controller {

	/** The server for the first Debenhams API server. */
	private static ISFTPManager sftpDebAPI1 = new SFTPManager("eap6", "176.74.183.149", 22, "prodapi", "/opt/jboss-eap-6.0/domain/servers/api01/log/");
	
	/** The server for the second Debenhams API server. */
	private static ISFTPManager sftpDebAPI2 = new SFTPManager("eap6", "176.74.183.138", 22, "prodapi", "/opt/jboss-eap-6.0/domain/servers/api02/log/");

	/**
	 * Render the home page.
	 *
	 * @return the HTTP result
	 */
	public static Result index() {
		return ok(index.render("Dolog"));
	}

	/**
	 * Uploads a file with POST.
	 *
	 * @return the HTTP result
	 */
	public static Result upload() {
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart rawFile = body.getFile("rawFile");
		if (rawFile != null) {
			String fileName = rawFile.getFilename();
			File file = rawFile.getFile();
			UploadedFile fileWrapper = new UploadedFile(file, fileName);
			Logger.log("UPLOADED FILE ID", fileWrapper.getID());
			SessionManager.addFile(fileWrapper);

			ObjectNode result = Json.newObject();
			result.put("id", fileWrapper.getID());
			result.put("name", fileWrapper.getName());
			result.put("size", fileWrapper.getSize());
			return ok(result);
		} else {
			flash("error", "Missing file");
			return null;
		}
	}

	/**
	 * Searches and processes an uploaded Debenhams API file based on the order ID.
	 *
	 * @param orderID the order id
	 * @return the HTTP result
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static Result process(Integer orderID) throws IOException, InterruptedException {
		List<IFileWrapper> files = SessionManager.getFiles();
		IProcessedFile processedFile = Helper.processFiles(files, orderID);
		if (processedFile == null) {
			return ok("No files to process: " + files.toString());
		}
		response().setContentType("application/octet-stream");
		return ok(Helper.createFile(processedFile));

	}

	/**
	 * Searches and processes Debenhams API files based on the order ID on the server.
	 *
	 * @param fileIDStrings the file id strings
	 * @param orderID the order id
	 * @return the HTTP result
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws JSchException the JSch exception
	 * @throws SftpException the SFTP exception
	 * @throws InterruptedException the interrupted exception
	 */
	public static Result processServer(String fileIDStrings, Integer orderID) throws IOException, JSchException, SftpException, InterruptedException {
		String[] fileIDs = fileIDStrings.split(",");
		List<ServerFile> sFiles1 = sftpDebAPI1.getFiles("zip");
		List<ServerFile> sFiles2 = sftpDebAPI2.getFiles("zip");
		List<IProcessedFile> processedFiles = new ArrayList<IProcessedFile>();

		for (String fileID : fileIDs) {
			ServerFile file1 = ServerFile.findServerFileFromName(sFiles1, fileID);
			file1.materialise(sftpDebAPI1);
			IProcessedFile pfile1 = Helper.processFile(file1, orderID);

			ServerFile file2 = ServerFile.findServerFileFromName(sFiles2, fileID);
			file2.materialise(sftpDebAPI2);
			IProcessedFile pfile2 = Helper.processFile(file2, orderID);

			IProcessedFile pfile = Merger.merge(pfile1, pfile2);
			processedFiles.add(pfile);
		}

		response().setContentType("application/octet-stream");
		File outputFile = Helper.createFile(processedFiles);
		ZipTool.closeAllZipFiles();
		return ok(outputFile);
	}

	/**
	 * Search an uploaded file based on a query and certain options.
	 *
	 * @param query the query
	 * @param removeDuplicates remove duplicates or not
	 * @param appendNewLine append new line or not
	 * @return the HTTP result
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static Result search(String query, boolean removeDuplicates, boolean appendNewLine) throws IOException, InterruptedException {
		ISearcher searcher = new Searcher(10);
		List<IFileWrapper> files = SessionManager.getFiles();
		StringBuilder sb = new StringBuilder();
		if (files.size() == 0) {
			return badRequest("No files uploaded");
		}
		for (IFileWrapper file : files) {
			// TODO: add zip detection and extraction
			searcher.setFile(file.getBufferedReader());
			List<SearchResult> results = new ArrayList<SearchResult>();

			Thread searchThread = searcher.scanDown(query.trim(), -1, (SearchResult result) -> {
				results.add(result);
			});

			searchThread.join();

			if (removeDuplicates) {
				SearchResult.removeDuplicates(results);
			}

			for (SearchResult result : results) {
				Logger.log(result);
				sb.append(result.matchedPart);
				if (appendNewLine) {
					sb.append("\r\n");
				}
			}
		}

		response().setContentType("application/octet-stream");
		return ok(sb.toString());
	}

	/**
	 * Delete an uploaded file.
	 *
	 * @return the HTTP result
	 */
	public static Result deleteFile() {
		JsonNode node = request().body().asJson();
		String fileID = node.path("fileID").asText();
		SessionManager.removeFile(fileID);
		return ok(fileID);
	}

	/**
	 * Gets the uploaded files.
	 *
	 * @return the uploaded files
	 */
	public static Result getUploadedFiles() {
		return ok(SessionManager.getSessionNode("fileIDs"));
	}

	/**
	 * Gets the API server files.
	 *
	 * @return the API server files
	 * @throws JSchException the j sch exception
	 * @throws SftpException the sftp exception
	 */
	public static Result getAPIServerFiles() throws JSchException, SftpException {
		List<ServerFile> sFiles = sftpDebAPI2.getFiles("zip");
		JsonNode node = JSONHelper.createEmptyJsonNode();
		JsonNode arrayNode = JSONHelper.addArrayNodeToJsonNode(node, "files");

		for (ServerFile file : sFiles) {
			Map<String, String> map = JSONHelper.generateJSONMap(new String[] { "id", "name", "size" }, new String[] { file.getID(), file.getName(), Long.toString(file.getSize()) });
			JSONHelper.addMapToJsonArrayNode(arrayNode, map);
		}

		return ok(node);
	}

	/**
	 * Gets the JavaScript routes.
	 * Used in the JQuery
	 *
	 * @return the HTTP result
	 */
	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsRoutes", controllers.routes.javascript.Application.upload(), controllers.routes.javascript.Application.getUploadedFiles(), controllers.routes.javascript.Application.process(), controllers.routes.javascript.Application.processServer(), controllers.routes.javascript.Application.getAPIServerFiles(), controllers.routes.javascript.Application.deleteFile()));
	}
}
