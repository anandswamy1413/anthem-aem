package com.anthem.platform.core.services.utility.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.request.RequestParameter;
import org.apache.sling.api.request.RequestParameterMap;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.xss.XSSAPI;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@SuppressWarnings("squid:S2221")
@Component(service = JacksonMapperService.class, immediate = true)
public class JacksonMapperServiceImpl implements JacksonMapperService {

	private static final Logger logger = LoggerFactory.getLogger(JacksonMapperServiceImpl.class);
		
	@Reference
	private XSSAPI xssAPI;
	
	@Override
	public Map<String, Object> convertJsonToMap(String jsonString) {
		Map<String, Object> map = new HashMap<>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			map = mapper.readValue(jsonString, Map.class);
		} catch (IOException e) {
			logger.error("Error converting Json to map ", e);
		}
		return map;
	}

	@Override
	public <T> Map<String, Object> convertObjectToMap(T model, Boolean includeNull) {
		Map<String, Object> map = new HashMap<>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			if(!includeNull.booleanValue()) mapper.setSerializationInclusion(Include.NON_NULL);
			TypeReference<HashMap<String, Object>> typeRef = new TypeReference<HashMap<String, Object>>() {
			};
			map.putAll(mapper.convertValue(model, typeRef));
		} catch (Exception e) {
			logger.error("Error converting object to map ",e);
		}
		return map;
	}

	@Override
	public <T> String convertObjectToJson(T model, Boolean includeNull) {
		String jsonString = PlatformConstants.EMPTY_JSON_OBJECT;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			if(!includeNull.booleanValue()) mapper.setSerializationInclusion(Include.NON_NULL);
			jsonString = mapper.writeValueAsString(model);
		} catch (IOException e) {
			logger.error("Error parsing JSON ",e);
		} 
		return jsonString;
	}

	@Override
	public <T> T convertJsonToObject(InputStream stream, Class<T> clazz) {
		T obj = null;
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(stream, clazz);
		} catch (IOException e) {
			logger.error("Error reading Stream value ",e);
		}
		return obj;
	}

	@Override
	public <T> T convertJsonToObject(String jsonString, Class<T> clazz) { 
		T obj = null;

			try{

				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				obj = mapper.readValue(jsonString, clazz);
			} catch (IOException e) {
				logger.error("Error reading Json value ",e);
			} 			
		return obj;
	
	}
	
	@Override
	public <T> T convertMapToObject(Map<?,?> map, Class<T> clazz) { 
		T obj = null;
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
			obj = mapper.convertValue(map, clazz); 			
		return obj;
	
	}
	
	@Override
	public <T> T convertRequestMapToObject(RequestParameterMap requestParameterMap, Class<T> clazz) {
		Map<String,Object> paramsMap = new HashMap<>();
		for (Map.Entry<String,RequestParameter[]> entry : requestParameterMap.entrySet()) {
			RequestParameter[] item = entry.getValue();	
			if(item.length >= 1) {
				paramsMap.put(entry.getKey(), sanitizedHtmlString(item[0].getString()));
			}		    
		}
		return convertMapToObject(paramsMap, clazz);
	}
	
	@Override
	public Map<String,String> sanitizedRequestMap(RequestParameterMap requestParameterMap) {
		Map<String,String> paramsMap = new HashMap<>();
		for (Map.Entry<String,RequestParameter[]> entry : requestParameterMap.entrySet()) {
			RequestParameter[] item = entry.getValue();	
			if(item.length >= 1) {
				paramsMap.put(entry.getKey(), sanitizedHtmlString(item[0].getString()));
			}		    
		}
		return paramsMap;
	}
	
	@Override
	public List<BasicNameValuePair> convertRequestMapToNameValuePair(RequestParameterMap requestParameterMap) {
		List<BasicNameValuePair> valuePairList = new ArrayList<>();
		for (Map.Entry<String,RequestParameter[]> entry : requestParameterMap.entrySet()) {
			RequestParameter[] item = entry.getValue();	
			if(item.length >= 1) {
				valuePairList.add(new BasicNameValuePair(entry.getKey(), item[0].getString()));
			}		    
		}
		return valuePairList;
	}

	@Override
	public String sanitizedJsonString(String jsonStr) {
		return xssAPI.getValidJSON(jsonStr, PlatformConstants.EMPTY_JSON_OBJECT);
	}
	
	@Override
	public String sanitizedHtmlString(String htmlString) {
		return xssAPI.filterHTML(htmlString);
	}

	@Override
	public <T> T sanitizeInputStreamToObject(InputStream stream, Class<T> clazz) {
		T obj = null;
		try {
			String inputString = IOUtils.toString(stream, StandardCharsets.UTF_8);
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			obj = mapper.readValue(sanitizedJsonString(inputString), clazz);
		} catch (IOException e) {
			logger.error("Error reading Sanitized String value ",e);
		}
		return obj;
	}

	@Override
	public <T> String mapToJson(Map<String, T> inputMap) {
		ObjectMapper mapperObj = new ObjectMapper();
		String jsonResp = StringUtils.EMPTY;
        try {
        	jsonResp = mapperObj.writeValueAsString(inputMap);
            return jsonResp;
        } catch (IOException e) {
        	logger.error("Error converting map to json ", e);
        }
		return jsonResp;
	}
	
	@Override
	public String valueMapToJson(ValueMap valueMap, String[] systemPropertiesToInclude) {
		Map<String,Object> outputMap = new HashMap<>();
		if(null != valueMap) {
			Iterator<Map.Entry<String, Object>> itr = valueMap.entrySet().iterator();
			while (itr.hasNext()) {
				Entry<String, Object> set = itr.next();
				if(ArrayUtils.contains(systemPropertiesToInclude, set.getKey())) {
					outputMap.put(set.getKey(), set.getValue());
				} else if(!StringUtils.startsWith(set.getKey(), "jcr:") || !StringUtils.startsWith(set.getKey(), "cq:") || !StringUtils.startsWith(set.getKey(), "sling:")) {
					outputMap.put(set.getKey(), set.getValue());
				}
			}			
		}
		
		return this.mapToJson(outputMap);
	}
	
	@Override
	public Map<String,Object> valueMapToMap(ValueMap valueMap, String[] systemPropertiesToInclude) {
		Map<String,Object> outputMap = new HashMap<>();
		if(null != valueMap) {
			Iterator<Map.Entry<String, Object>> itr = valueMap.entrySet().iterator();
			while (itr.hasNext()) {
				Entry<String, Object> set = itr.next();
				if(ArrayUtils.contains(systemPropertiesToInclude, set.getKey())) {
					outputMap.put(set.getKey(), set.getValue());
				} else if(!StringUtils.startsWith(set.getKey(), "jcr:") || !StringUtils.startsWith(set.getKey(), "cq:") || !StringUtils.startsWith(set.getKey(), "sling:")) {
					outputMap.put(set.getKey(), set.getValue());
				}
			}			
		}
		
		return outputMap;
	}
}
