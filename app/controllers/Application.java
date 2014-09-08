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
import model.play.helpers.UploadedZipFile;

import org.apache.commons.io.FilenameUtils;
import org.dolan.datastructures.IProcessedFile;
import org.dolan.merger.Merger;
import org.dolan.remoteaccess.ISFTPManager;
import org.dolan.remoteaccess.SFTPManager;
import org.dolan.tools.LogTool;
import org.dolan.ziptools.ZipTool;

import play.Routes;
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
 * The Class Application. This is the main entry point for the application. All
 * HTTP routes start at this.
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
		LogTool.log("Begin uploading");
		MultipartFormData body = request().body().asMultipartFormData();
		FilePart rawFile = body.getFile("rawFile");

		if (rawFile == null) {
			LogTool.error("Upload failed");
			flash("error", "Missing file");
			return badRequest("Missing file");
		}

		String fileName = rawFile.getFilename();
		File file = rawFile.getFile();

		String fileType = FilenameUtils.getExtension(fileName);
		LogTool.log("Uploaded file is of type", fileType);

		IFileWrapper fileWrapper = null;
		if (fileType.equals("zip")) {
			fileWrapper = new UploadedZipFile(file, fileName);
		} else {
			fileWrapper = new UploadedFile(file, fileName);
		}
		LogTool.log("Adding uploaded file to cache", fileWrapper.getName());
		ObjectNode result = SessionManager.addFile(fileWrapper);
		LogTool.log("Finished uploading");
		return ok(result);

	}

	/**
	 * Searches and processes an uploaded Debenhams API file based on the order
	 * ID.
	 *
	 * @param orderID the order id
	 * @return the HTTP result
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws InterruptedException the interrupted exception
	 */
	public static Result process(Integer orderID) throws IOException, InterruptedException {
		LogTool.log("Begin processing uploaded files");
		if (!validateOrderID(orderID)) {
			LogTool.error("Invalid order ID passed", orderID);
			return badRequest("Invalid orderID");
		}
		LogTool.log("Finding uploaded files");
		List<IFileWrapper> files = SessionManager.getFiles();
		LogTool.log("Searching files for orderID", files);

		IProcessedFile processedFile = DebenhamsAPIHelper.processFiles(files, orderID);

		if (processedFile == null) {
			LogTool.log("Files do not contain orderID");
			return ok("Files do not contain orderID: " + files.toString());
		} else {
			response().setContentType("application/octet-stream");
			LogTool.log("Finished searching. Creating file now.");
			return ok(Helper.createFile(processedFile));
		}
	}

	/**
	 * Searches and processes Debenhams API files based on the order ID on the
	 * server.
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
		LogTool.log("Begin processing API server files");
		if (!validateOrderID(orderID)) {
			LogTool.error("Invalid order ID passed", orderID);
			return badRequest("Invalid orderID");
		}

		String[] fileIDs = fileIDStrings.split(",");
		List<ServerFile> sFiles1 = sftpDebAPI1.getFiles("zip");
		List<ServerFile> sFiles2 = sftpDebAPI2.getFiles("zip");
		List<IProcessedFile> processedFiles = new ArrayList<IProcessedFile>();

		for (String fileID : fileIDs) {
			LogTool.log("Finding file based on fileID", fileID);

			ServerFile file1 = ServerFile.findServerFileFromName(sFiles1, fileID);
			LogTool.log("Found and will materialise", file1);
			file1.materialise(sftpDebAPI1);
			LogTool.log("Processing file", file1);
			IProcessedFile pfile1 = DebenhamsAPIHelper.processFile(file1, orderID);

			ServerFile file2 = ServerFile.findServerFileFromName(sFiles2, fileID);
			LogTool.log("Found and will materialise", file2);
			file2.materialise(sftpDebAPI2);
			LogTool.log("Processing file", file2);
			IProcessedFile pfile2 = DebenhamsAPIHelper.processFile(file2, orderID);

			LogTool.log("Merging the 2 server files together");
			IProcessedFile pfile = Merger.merge(pfile1, pfile2);
			LogTool.log("Adding merged file to list of processed files");
			processedFiles.add(pfile);
		}

		File outputFile = Helper.createFile(processedFiles);
		if (outputFile == null) {
			LogTool.log("Nothing found with orderID", orderID);
			LogTool.log("Finish processing API server files");
			return ok("Nothing found with orderID: " + orderID);
		} else {
			LogTool.log("Creating downloadable file");
			ZipTool.closeAllZipFiles();
			LogTool.log("Finish processing API server files");
			response().setContentType("application/octet-stream");
			return ok(outputFile);
		}
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
		LogTool.log("Begin searching");
		List<IFileWrapper> files = SessionManager.getFiles();
		LogTool.log("Searching files", files);
		if (files.isEmpty()) {
			LogTool.log("No files uploaded");
			return ok("No files uploaded");
		}
		String sb = SearcherHelper.search(files, query, removeDuplicates, appendNewLine);
		if (sb.isEmpty()) {
			LogTool.log("Cannot find anything in files");
			return ok("Cannot find anything in files");
		}
		response().setContentType("application/octet-stream");
		LogTool.log("Finish searching");
		return ok(sb);
	}

	/**
	 * Delete an uploaded file.
	 *
	 * @return the HTTP result
	 */
	public static Result deleteFile() {
		LogTool.log("Begin deleting file");
		JsonNode node = request().body().asJson();
		String fileID = node.path("fileID").asText();
		LogTool.log("Deleting", fileID);
		SessionManager.removeFile(fileID);
		LogTool.log("Finish deleting");
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
		LogTool.log("Begin retreiving server files");
		List<ServerFile> sFiles = sftpDebAPI2.getFiles("zip");
		JsonNode node = JSONHelper.createEmptyJsonNode();
		JsonNode arrayNode = JSONHelper.addArrayNodeToJsonNode(node, "files");

		LogTool.log("Creating JSON for the server files");
		for (ServerFile file : sFiles) {
			Map<String, String> map = JSONHelper.generateJSONMap(new String[] { "name", "size", "date" }, new String[] { file.getName(), Long.toString(file.getSize()), file.getDate() });
			JSONHelper.addMapToJsonArrayNode(arrayNode, map);
		}
		LogTool.log("Finish retreiving server files");

		return ok(node);
	}

	/**
	 * Gets the JavaScript routes. Used in the JQuery
	 *
	 * @return the HTTP result
	 */
	public static Result javascriptRoutes() {
		response().setContentType("text/javascript");
		return ok(Routes.javascriptRouter("jsRoutes", controllers.routes.javascript.Application.upload(), controllers.routes.javascript.Application.getUploadedFiles(), controllers.routes.javascript.Application.process(), controllers.routes.javascript.Application.processServer(), controllers.routes.javascript.Application.getAPIServerFiles(), controllers.routes.javascript.Application.deleteFile()));
	}

	private static boolean validateOrderID(Integer orderID) {
		if (orderID < 0 && orderID.toString().length() < 8) {
			return false;
		} else {
			return true;
		}
	}
}
