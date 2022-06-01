package com.anthem.platform.core.services.endpoints;

import java.util.Map;


public interface ApiEndpointService {

	public Map<String, String> getEndpointConfigs() ;
	
	public String get(String key); 
	
	public void add(String key,String value);
	
	public String getEndpointId();
	
}
