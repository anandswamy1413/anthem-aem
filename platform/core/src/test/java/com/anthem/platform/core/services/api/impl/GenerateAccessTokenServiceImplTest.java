package com.anthem.platform.core.services.api.impl;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.BaseRequest;
import com.anthem.platform.core.beans.OAuthResponse;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.api.configs.GenerateAccessTokenServiceConfig;
import com.anthem.platform.core.services.endpoints.ApiEndpointFactory;
import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.services.utility.RestClient;
import com.anthem.platform.core.utils.PlatformUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

class GenerateAccessTokenServiceImplTest {

	@InjectMocks
	private GenerateAccessTokenServiceImpl generateaccesstokenserviceimpl;
	
	@Mock
	private GenerateAccessTokenServiceConfig config;
	
	@Mock
	 private Logger log;
	
	@Mock
	 private APIResponse response;
	
	@Mock
	private  OAuthResponse oAuthResponse;
	
	@Mock
	private ObjectMapper mapper;
	
	@Mock
	private  JSONObject obj;
	
	@Mock
    private ApiEndpointFactory apiEndpointFactory;
	
	@Mock
	private ApiEndpointService apiEndpointService;
    
	@Mock
	private RestClient restClient;
	 
	@Mock
	private BaseRequest authRequest;
	
	@Mock
	private JacksonMapperService jacksonMapper;
	
	@BeforeEach
	void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		when(config.endpointId()).thenReturn("Test_apigee");
		when(apiEndpointFactory.getEndpoint("Test_apigee")).thenReturn(apiEndpointService);
		when(config.apigeeAccessTokenEndpoint()).thenReturn("/v1/oauth/accesstoken");
		when(apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.APIGEE_HOST_URL.name())).thenReturn("https://test.com");
		generateaccesstokenserviceimpl.activate(config);
		List<BasicNameValuePair> bodyParams = new LinkedList<>();
		bodyParams.add(new BasicNameValuePair(PlatformConstants.SCOPE, PlatformConstants.PUBLIC));
    	bodyParams.add(new BasicNameValuePair(PlatformConstants.GRANT_TYPE, PlatformConstants.CLIENT_CREDS));
		when(PlatformUtils.getUrlParams(bodyParams)).thenReturn("scope=public&grant_type=client_credentials");
		String a="{Authorization=Basic bnVsbDpudWxs, apikey=null, Content-Type=application/x-www-form-urlencoded}";
		when(apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.APIGEE_API_KEY.name())).thenReturn("test_key");
		Map<String, String> authHeader = new HashMap<>();
	     authHeader.put(PlatformConstants.APIKEY,"test_key");
	     authHeader.put(HttpHeaders.AUTHORIZATION,"Basic bnVsbDpudWxs");
	     authHeader.put(HttpHeaders.CONTENT_TYPE,"application/x-www-form-urlencoded");
	     when(restClient.sendRequest(Mockito.any(BaseRequest.class),Mockito.anyMap())).thenReturn(response); 
	     when(response.getStatusCode()).thenReturn(200);
	     when(response.getResponse()).thenReturn("response");
	     when(jacksonMapper.convertJsonToObject(response.getResponse(), OAuthResponse.class)).thenReturn(oAuthResponse);
	     when(oAuthResponse.getAccessToken()).thenReturn("Sample token");
	    
	}
	
	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.GenerateAccessTokenServiceImpl#generateNewToken()}.
	 */	
	@Test
	void testGenerateNewToken() 
	{  final String expected="Sample token";
	  String actual=generateaccesstokenserviceimpl.generateNewToken();
	  assertEquals(expected, actual);
	}
	
	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.GenerateAccessTokenServiceImpl#getToken()}.
	 */	
	  @Test 
	  void testGetToken() 
	  { final String expected="Sample token";
		 String actual=generateaccesstokenserviceimpl.getToken();
		 assertEquals(expected, actual);
	  }
	 

}
