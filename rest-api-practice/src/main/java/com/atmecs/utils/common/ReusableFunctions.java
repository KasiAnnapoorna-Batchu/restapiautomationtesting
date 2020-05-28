package com.atmecs.utils.common;



import com.jayway.jsonpath.JsonPath;

public class ReusableFunctions {
	/**
	 * Compiles JsonPath with expression that is passed.
	 * 
	 */
	public JsonPath jsonCompile(String expression) {
		// Compiles expression that is being passed.
		JsonPath jsonValue = JsonPath.compile(expression);
		return jsonValue;

	}

}

