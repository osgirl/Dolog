package model.play.helpers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dolan.tools.Logger;

import play.cache.Cache;
import play.libs.Json;
import play.mvc.Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The Class SessionManager.
 * A wrapper class which handles all the Play Framework caching/cookies/session data.
 */
public class SessionManager {

	/**
	 * Adds the file to the cache and session.
	 *
	 * @param file the file
	 * @return the JSON node
	 */
	public static ObjectNode addFile(IFileWrapper file) {
		JsonNode rootNode = getSessionNode("fileIDs");
		JsonNode fileIDs = rootNode.path("files");
		
		int count = 1;
		if (file instanceof UploadedZipFile) {
			count = ((UploadedZipFile)file).getAllBufferedReaders().size();
		}

		Map<String, String> map = JSONHelper.generateJSONMap(new String[] { "id", "name", "size", "count" }, new String[] { file.getID(), file.getName(), Long.toString(file.getSize()), Integer.toString(count) });

		JSONHelper.addMapToJsonArrayNode(fileIDs, map);
		Cache.set(file.getID(), file, 1800);
		Controller.session("fileIDs", rootNode.toString());
		
		ObjectNode result = Json.newObject();
		result.put("id", file.getID());
		result.put("name", file.getName());
		result.put("size", file.getSize());
		result.put("count", count);

		return result;
	}

	/**
	 * Removes the file from the cache and session.
	 *
	 * @param id the id
	 */
	public static void removeFile(String id) {
		JsonNode rootNode = getSessionNode("fileIDs");
		JsonNode fileIDs = rootNode.path("files");
		JSONHelper.removeElementFromJsonArrayNode(fileIDs, "id", id);
		Controller.session("fileIDs", rootNode.toString());
	}

	/**
	 * Gets the files from cache.
	 *
	 * @param array the array
	 * @return the files from cache
	 */
	private static List<IFileWrapper> getFilesFromCache(JsonNode array) {
		ArrayNode arrayNode = (ArrayNode) array;
		Iterator<JsonNode> ite = arrayNode.elements();
		List<IFileWrapper> files = new ArrayList<IFileWrapper>();

		while (ite.hasNext()) {
			JsonNode temp = ite.next();
			Logger.log("CACHE VALUE", temp.findValue("id").asText());
			Object cacheObj = Cache.get(temp.findValue("id").asText());
			if (cacheObj == null) {
				continue;
			}
			IFileWrapper file = (IFileWrapper) cacheObj;
			files.add(file);
		}

		return files;
	}

	/**
	 * Gets the files from session.
	 *
	 * @return the files
	 */
	public static List<IFileWrapper> getFiles() {
		JsonNode rootNode = getSessionNode("fileIDs");
		JsonNode filesJSON = rootNode.path("files");
		List<IFileWrapper> files = getFilesFromCache(filesJSON);

		return files;
	}

	/**
	 * Gets the session JSON node.
	 *
	 * @param key the key
	 * @return the session node
	 */
	public static JsonNode getSessionNode(String key) {
		String jsonString = Controller.session(key);
		JsonNode rootNode = null;
		if (jsonString == null) {
			rootNode = JSONHelper.createEmptyJsonNode();
			JSONHelper.addArrayNodeToJsonNode(rootNode, "files");
			Controller.session(key, jsonString);
		} else {
			rootNode = Json.parse(jsonString);
		}

		return rootNode;
	}

}
