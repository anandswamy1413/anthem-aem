package com.anthem.platform.core.beans;

import java.util.HashMap;
import java.util.Map;

public class PostBackResponse {
	
	private String status;
		
	private int httpStatusCode;
	
	private Map<String,Object> errorData;
	
	private Map<String,Object> successData;
	
	public PostBackResponse(Status status, int httpStatusCode) {
		this.status = status.name();
		this.httpStatusCode = httpStatusCode;
		this.errorData = new HashMap<>();
		this.successData = new HashMap<>();
	}

	public String getStatus() {
		return status;
	}

	public PostBackResponse setStatus(String status) {
		this.status = status;
		return this;
	}

	public Map<String, Object> getErrorData() {
		return errorData;
	}

	public void setErrorData(Map<String, Object> errorData) {
		this.errorData = errorData;
	}

	public Map<String, Object> getSuccessData() {
		return successData;
	}

	public void setSuccessData(Map<String, Object> successData) {
		this.successData = successData;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}
	
	public PostBackResponse setHttpStatusCode(int httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
		return this;
	}
	
	public PostBackResponse addErrorCode(String errorCode) {
		this.errorData.put("errorCode", errorCode);
		return this;
	}
	
	public PostBackResponse addErrorMsg(String errorMsg) {
		this.errorData.put("errorMsg", errorMsg);
		return this;
	}
	
	public enum Status {
		SUCCESS,
		ERROR,
		REDIRECT
	}
	
}
