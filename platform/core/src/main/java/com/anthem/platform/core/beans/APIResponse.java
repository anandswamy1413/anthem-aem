package com.anthem.platform.core.beans;


public class APIResponse {

    private String response;

    private int statusCode;
    
   
	public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
