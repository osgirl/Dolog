package model.play.helpers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dolan.tools.LogTool;

import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * The Class JSONHelper.
 * A wrapper which will handle all the JSON related calls.
 */
public class JSONHelper {

	/**
	 * Creates the empty JSON node.
	 *
	 * @return the JSON node
	 */
	public static JsonNode createEmptyJsonNode() {
		return JsonNodeFactory.instance.objectNode();
	}

	/**
	 * Adds the array node to JSON node.
	 *
	 * @param node the node
	 * @param name the name
	 * @return the JSON node
	 */
	public static JsonNode addArrayNodeToJsonNode(JsonNode node, String name) {
		((ObjectNode) node).putArray(name);
		return node.path(name);
	}

	/**
	 * Adds the element to JSON node.
	 *
	 * @param node the node
	 * @param key the key
	 * @param value the value
	 * @return the JSON node
	 */
	public static JsonNode addElementToJsonNode(JsonNode node, String key, String value) {
		return ((ObjectNode) node).put(key, value);
	}

	/**
	 * Generate JSON map.
	 *
	 * @param keys the keys
	 * @param values the values
	 * @return the map
	 */
	public static Map<String, String> generateJSONMap(String[] keys, String[] values) {
		LogTool.trace("Begin generating JSON Map");
		if (keys.length != values.length) {
			RuntimeException keysException = new RuntimeException("Keys and values mismatch");
			LogTool.error("Keys and values mismatch", keysException);
			throw keysException;
		}

		Map<String, String> map = new HashMap<String, String>();

		for (int i = 0; i < keys.length; i++) {
			LogTool.trace("Putting key " + keys[i] + " and value " + values[i] + " into Map");
			map.put(keys[i], values[i]);
		}
		LogTool.trace("Finish genrating JSON Map");
		return map;
	}

	/**
	 * Adds the map to JSON array node.
	 *
	 * @param arrayNode the array node
	 * @param map the map
	 */
	public static void addMapToJsonArrayNode(JsonNode arrayNode, Map<String, String> map) {
		LogTool.trace("Begin adding map to JSON Array node", map);
		ObjectNode fileDetails = Json.newObject();

		for (Map.Entry<String, String> entry : map.entrySet()) {
			LogTool.trace("Adding key " + entry.getKey() + " and value " + entry.getValue() + " into JSON array node");
			fileDetails.put(entry.getKey(), entry.getValue());
		}
		LogTool.trace("Adding array node into parent node");
		((ArrayNode) arrayNode).add(fileDetails);
		LogTool.trace("Modified node", arrayNode);
	}

	/**
	 * Removes the element from JSON array node.
	 *
	 * @param array the array
	 * @param key the key
	 * @param value the value
	 */
	public static void removeElementFromJsonArrayNode(JsonNode array, String key, String value) {
		LogTool.trace("Begin removing element " + key + " from JSON array node");
		ArrayNode arrayNode = (ArrayNode) array;
		Iterator<JsonNode> ite = arrayNode.elements();

		int count = 0;
		while (ite.hasNext()) {
			String tempVal = ite.next().findValue(key).asText();
			if (tempVal.equals(value)) {
				LogTool.trace("Removing element " + key + " with value " + value);
				arrayNode.remove(count);
				break;
			}
			count++;
		}
		LogTool.trace("Finish removing element from JSON Array node");
	}
}
