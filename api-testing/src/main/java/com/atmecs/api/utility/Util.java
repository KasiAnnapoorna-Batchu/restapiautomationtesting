package com.atmecs.api.utility;

import com.jayway.jsonpath.JsonPath;

public class Util {
	
	public JsonPath jsonCompile(String expression) {
		// Compiles expression that is being passed.
		JsonPath jsonValue = JsonPath.compile(expression);
		return jsonValue;

	}

}
