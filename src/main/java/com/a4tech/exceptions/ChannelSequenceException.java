package com.a4tech.exceptions;

public class ChannelSequenceException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4400535610130639139L;
	private String errorMsg;

	public ChannelSequenceException(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

}
