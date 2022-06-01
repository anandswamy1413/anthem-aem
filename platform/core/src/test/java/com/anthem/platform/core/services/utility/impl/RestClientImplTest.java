package com.anthem.platform.core.services.utility.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.service.component.annotations.Reference;

import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.BaseRequest;
import com.anthem.platform.core.beans.HttpMethodType;
import com.anthem.platform.core.services.api.AnthemConnectorConfigService;
import com.anthem.platform.core.utils.PlatformHelper;

import acscommons.com.jcraft.jsch.Logger;

  class RestClientImplTest {

	private final String REQUEST_URL = "https://api-np.anthem.com/v1/coreutility/pharmacy/search";
	private final String APIKEY = "apikey";
	private final String API_GEE_API_KEY = "H4mPESl6mEqk0M8iZnC5oF4IoUmVGFds";
	
	@InjectMocks
	private RestClientImpl restClientImpl;
	
	@Mock
	private CloseableHttpClient httpClient;

	@Mock
	private PoolingHttpClientConnectionManager cm;

	@Mock
	private AnthemConnectorConfigService configService;
	
	@Mock
	private CloseableHttpResponse httpResponse;
	
	@Mock
	private BaseRequest baseRequest;
	
	@Mock
	private HttpMethodType methodType;
	
	private final Map<String, String> headerMap  = new HashMap<>();
	
	@Mock
	private StringEntity requestEntity;
	
	@Mock
	private HttpPost postMethod;
	
	@Mock
	private org.slf4j.Logger log;
	
	@Mock
    private PlatformHelper platformHelper;
	
	@BeforeEach
	void setUp() throws Exception 
	{ 
		MockitoAnnotations.initMocks(this);
		restClientImpl.activate();
		headerMap.put(HttpHeaders.AUTHORIZATION, "Bearer " + "");
        headerMap.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());
        headerMap.put(APIKEY, API_GEE_API_KEY);
	  
	}

	@Test
	void testGetResponsePostMethod() throws MethodNotSupportedException, IOException 
	{
		BaseRequest request = new BaseRequest(REQUEST_URL,HttpMethodType.POST, "");
		APIResponse actual = restClientImpl.sendRequest(request,headerMap);
		assertNotNull(restClientImpl);
		assertNotNull(actual);
	}
	
	@Test
	void testGetResponsePatchMethod() throws MethodNotSupportedException, IOException 
	{
		BaseRequest request = new BaseRequest(REQUEST_URL,HttpMethodType.PATCH, "");
		APIResponse actual = restClientImpl.sendRequest(request,headerMap);
		assertNotNull(restClientImpl);
		assertNotNull(actual);
	}
	
	@Test
	void testGetResponseGetMethod() throws MethodNotSupportedException, IOException 
	{
		BaseRequest request = new BaseRequest(REQUEST_URL,HttpMethodType.GET, "");
		APIResponse actual = restClientImpl.sendRequest(request,headerMap);
		assertNotNull(restClientImpl);
		assertNotNull(actual);
	}
	
	@Test
	void testGetResponseMethodNotsupported() 
	{ 	
			try {
				BaseRequest request = new BaseRequest(REQUEST_URL,HttpMethodType.DELETE, "");
				restClientImpl.sendRequest(request,headerMap);
			} catch (MethodNotSupportedException | IOException e)
			  {
				
			  log.error("Method Not Supported");
			}
			assertNotNull(restClientImpl);
	}
	
	@Test
	void testDeactivate() 
	{
		restClientImpl.deactivate();
		assertNotNull(restClientImpl);
	}
	
}
