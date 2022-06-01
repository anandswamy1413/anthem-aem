package com.anthem.platform.core.beans;

public class ApiException extends Exception {
	
	private static final long serialVersionUID = 1L;
	
	private final String errorCode;
	
	private final String errorMsg;
	
	public ApiException(String errorCode, String errorMsg) {
		super(errorMsg);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	
	public ApiException(String errorCode, String errorMsg, Exception cause) {
		super(errorMsg, cause);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	public String getErrorCode() {
		return errorCode;
	}


	public String getErrorMsg() {
		return errorMsg;
	}

	
	

}
