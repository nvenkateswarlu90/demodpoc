package com.a4tech.exceptions;

public class MapServiceRequestDeniedException extends Exception {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6102228911826472456L;
	private String errorMsg;

	public MapServiceRequestDeniedException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}
