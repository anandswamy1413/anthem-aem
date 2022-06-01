package com.anthem.platform.core.services.utility;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.ValueMap;

public interface JacksonMapperService {
	
	Map<String, Object> convertJsonToMap(String jsonStr);
	
	<T> Map<String, Object> convertObjectToMap(T model, Boolean includeNull);
	
	<T> String convertObjectToJson(T model, Boolean includeNull);
	
	<T> T convertJsonToObject(InputStream stream, Class<T> clazz);
	
	<T> T convertJsonToObject(String string, Class<T> clazz);
	
	<T> T convertMapToObject(Map<?,?> map, Class<T> clazz);
	
	<T> T convertRequestMapToObject(RequestParameterMap requestParameterMap, Class<T> clazz);
	
	List<BasicNameValuePair> convertRequestMapToNameValuePair(RequestParameterMap requestParameterMap);
		
	String sanitizedJsonString(String jsonString);
	
	String sanitizedHtmlString(String htmlString);

	<T> T sanitizeInputStreamToObject(InputStream stream, Class<T> clazz);
	
	Map<String,String> sanitizedRequestMap(RequestParameterMap requestParameterMap);
	
	<T> String mapToJson(Map<String, T> inputMap);
	
	String valueMapToJson(ValueMap valueMap, String[] systemPropertiesToInclude);
	
	Map<String,Object> valueMapToMap(ValueMap valueMap, String[] systemPropertiesToInclude);
}
