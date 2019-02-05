package com.bh.api.proxy.gateway;

import java.util.List;

import org.junit.Test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

public class JsonReaderTest {

	@Test
	public void test_jsonReader() throws Exception {
		
		String jsonResponse = "{\"a\":\"a\",\"num\":10,\"b\":[{\"c\":\"c1\"},{\"c\":\"c2\"},{\"c\":\"c3\"}],\"e\":[{\"e\":[{\"f\":\"f1\"},{\"f\":\"f2\"}]},{\"f\":[{\"g\":\"g1\"},{\"g\":\"g2\"}]}]}";
		String path = "$.b[*].c";
		String path1 = "$.e[*].e[*].f";
		String path2 = "$.a";
		String path3 = "$.num";
		DocumentContext jsonDocumentContext= JsonPath.parse(jsonResponse);
		Object st = jsonDocumentContext.read(path3);
			
			if(st instanceof Integer) {
				System.out.println(st);
			}else if(st instanceof Double) {
				System.out.println(st);
			}
			if(st instanceof String) {
				System.out.println(st);
			}else if(st instanceof List) {
				System.out.println(st);
					System.out.println(!((List)st).stream().
							filter(item -> 
							{
								System.out.println("item: "+ item.toString() + "||" + item.toString().matches("([A-Za-z]+[0-9]|[0-9]+[A-Za-z])[A-Za-z0-9]*"));
								return !(item.toString().matches("([A-Za-z]+[0-9]|[0-9]+[A-Za-z])[A-Za-z0-9]*"));
								}
							).findFirst().isPresent());
				
			}
		
	}
}
