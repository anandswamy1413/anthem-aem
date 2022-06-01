package com.anthem.platform.core.services.api;

import java.util.Map;

import com.anthem.platform.core.beans.ApiException;

public interface ApiService <T,V> {
	
	public abstract String getApiId();

	public abstract String getDesc();

	public abstract V getResults(T apiData, Map<String,Object> metadata) throws ApiException;

	public abstract V updateData(T apiData, Map<String, Object> metadata) throws ApiException;
	
	public abstract String getEndpointData(String key);

}
