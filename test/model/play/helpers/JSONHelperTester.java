package model.play.helpers;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONHelperTester {

	@Test
	public void testIfCanRemoveFirstElementFromArray() throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		// JsonFactory factory = mapper.getFactory(); // since 2.1 use
		// mapper.getFactory() instead
		// JsonParser jp =
		// factory.createParser("{\"files\": [{\"id\": \"af880108-1794-4339-a5f7-d1fc9024e1da\",\"name\": \"countries.txt\",\"size\": 3349},{\"id\": \"f2b3a627-29b1-4a7b-b5d7-5697a86223e0\",\"name\": \"jiralog.txt\",\"size\": 2067671}]}");
		// JsonNode actualObj = mapper.readTree(jp);

		JsonNode actualObj = mapper.readTree("{\"files\": [{\"id\": \"af880108-1794-4339-a5f7-d1fc9024e1da\",\"name\": \"countries.txt\",\"size\": 3349},{\"id\": \"f2b3a627-29b1-4a7b-b5d7-5697a86223e0\",\"name\": \"jiralog.txt\",\"size\": 2067671}]}");
		JsonNode files = actualObj.path("files");

		System.out.println(files.toString());
		JSONHelper.removeElementFromJsonArrayNode(files, "id", "af880108-1794-4339-a5f7-d1fc9024e1da");
		System.out.println(files.toString());
		assertEquals("[{\"id\":\"f2b3a627-29b1-4a7b-b5d7-5697a86223e0\",\"name\":\"jiralog.txt\",\"size\":2067671}]", files.toString());
	}

	@Test
	public void testIfCanRemoveLastElementFromArray() throws JsonParseException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		JsonNode actualObj = mapper.readTree("{\"files\": [{\"id\": \"af880108-1794-4339-a5f7-d1fc9024e1da\",\"name\": \"countries.txt\",\"size\": 3349},{\"id\": \"f2b3a627-29b1-4a7b-b5d7-5697a86223e0\",\"name\": \"jiralog.txt\",\"size\": 2067671}]}");
		JsonNode files = actualObj.path("files");

		System.out.println(files.toString());
		JSONHelper.removeElementFromJsonArrayNode(files, "id", "f2b3a627-29b1-4a7b-b5d7-5697a86223e0");
		System.out.println(files.toString());
		assertEquals("[{\"id\":\"af880108-1794-4339-a5f7-d1fc9024e1da\",\"name\":\"countries.txt\",\"size\":3349}]", files.toString());
	}

	@Test
	public void testIfCanRemoveFromSingleArray() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();

		JsonNode actualObj = mapper.readTree("{\"files\": [{\"id\": \"af880108-1794-4339-a5f7-d1fc9024e1da\",\"name\": \"countries.txt\",\"size\": 3349}]}");
		JsonNode files = actualObj.path("files");

		System.out.println(files.toString());
		JSONHelper.removeElementFromJsonArrayNode(files, "id", "af880108-1794-4339-a5f7-d1fc9024e1da");
		System.out.println(files.toString());
		assertEquals("[]", files.toString());
	}

	@Test
	public void testIfCanCreateArrayNodeAndAdd() {
		JsonNode node = JSONHelper.createEmptyJsonNode();
		JsonNode arrayNode = JSONHelper.addArrayNodeToJsonNode(node, "blah");
		Map<String, String> map = JSONHelper.generateJSONMap(new String[] { "id", "name", "size" }, new String[] { "45345", "tempFile.txt", "456464" });
		JSONHelper.addMapToJsonArrayNode(arrayNode, map);
		System.out.println(arrayNode.toString());
		assertEquals("[{\"size\":\"456464\",\"name\":\"tempFile.txt\",\"id\":\"45345\"}]", arrayNode.toString());
	}

	@Test
	public void testIfCanCreateArray() {
		JsonNode node = JSONHelper.createEmptyJsonNode();
		JSONHelper.addArrayNodeToJsonNode(node, "noob");
		JSONHelper.addArrayNodeToJsonNode(node, "new");
		System.out.println(node);
		assertEquals("{\"noob\":[],\"new\":[]}", node.toString());
	}
}
