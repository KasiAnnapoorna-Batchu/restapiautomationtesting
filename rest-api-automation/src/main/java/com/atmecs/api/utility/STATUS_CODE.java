package com.atmecs.api.utility;

/**
 * 
 * @author Kasi.Batchu
 * 
 *  Status code enum values with different types of values.
 */
public enum STATUS_CODE {
	STATUS_200(200),STATUS_204(204),STATUS_400(400),STATUS_404(404),STATUS_500(500),STATUS_201(201),STATUS_401(401);
	int value;
	private STATUS_CODE(int value) {
		this.value=value;
	}
	public int getValue(){
		return this.value;
	}

}
