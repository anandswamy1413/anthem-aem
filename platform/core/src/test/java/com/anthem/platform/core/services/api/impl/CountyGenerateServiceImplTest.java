package com.anthem.platform.core.services.api.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import org.slf4j.Logger;

import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.beans.BaseRequest;
import com.anthem.platform.core.beans.CountyBean;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.api.GenerateAccessTokenService;
import com.anthem.platform.core.services.api.configs.CountyGenerateServiceConfig;
import com.anthem.platform.core.services.endpoints.ApiEndpointFactory;
import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.services.utility.RestClient;

import acscommons.com.google.common.net.HttpHeaders;

class CountyGenerateServiceImplTest {

	@InjectMocks
	private CountyGenerateServiceImpl countyGenerateServiceimpl;
	
	@Mock
	private GenerateAccessTokenService generateAccessTokenService;

	@Mock
    private RestClient restClient;
	
	@Mock
	private JacksonMapperService jacksonMapper;
	
	@Mock
	private ApiEndpointFactory apiEndpointFactory;
	
	@Mock
	private ApiEndpointService apiEndpointService;
	
    @Mock
    private Logger log;
	
    @Mock
    private CountyGenerateServiceConfig config;
    
    @Mock
    private Map<String, Object> metadata;
   
    @Mock
    private APIResponse response;
	
    @Mock
    private List<CountyBean> countyList;
    
    @Mock
    private BaseRequest request;
    
    @Mock
    private Map<String, String> headerMap ;
    
    @BeforeEach
	void setUp() throws Exception 
	{
	  MockitoAnnotations.initMocks(this);  
	  when(config.endpointId()).thenReturn("test_endpoint");
	  when(apiEndpointFactory.getEndpoint("test_endpoint")).thenReturn(apiEndpointService);
	  when(apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.APIGEE_HOST_URL.name())).thenReturn("APIGEE_HOST_URL");
	  when(config.countyPopulationEndpoint()).thenReturn( "/v1/coreutility/zipcode/details");
	  countyGenerateServiceimpl.activate(config);
	  when(generateAccessTokenService.getToken()).thenReturn("Sample token");
	  when(apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.APIGEE_API_KEY.name())).thenReturn("APIGEE_API_KEY");
	  headerMap.put(HttpHeaders.AUTHORIZATION, PlatformConstants.BEARER_SPACE+ "Sample token");
      headerMap.put(PlatformConstants.APIKEY, this.apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.APIGEE_API_KEY.name()));
      when(restClient.sendRequest(Mockito.any(BaseRequest.class), Mockito.anyMap())).thenReturn(response);
	  when(response.getStatusCode()).thenReturn(200);
	  when(jacksonMapper.convertJsonToObject(response.getResponse(), List.class)).thenReturn(countyList);
	}

    
    /**
	 * Test method for
	 * {@link com.anthem.platform.core.services.api.impl.CountyGenerateServiceImpl#getApiId()}.
	 */ 
	@Test
	void testGetApiId()
	{
		String actual=countyGenerateServiceimpl.getApiId();
		assertEquals("County Population", actual);
	}

	
	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.api.impl.CountyGenerateServiceImpl#getDesc()}.
	 */ 	
	@Test
	void testGetDesc()
	{
	  String actual=countyGenerateServiceimpl.getDesc();
	  assertEquals("Service to get county data based on zipcode", actual);
	}


	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.api.impl.CountyGenerateServiceImpl#getResults()}.
	 */ 
	@Test
	void testGetResults() throws ApiException 
	{ 
		assertEquals(countyList,countyGenerateServiceimpl.getResults("test-data", metadata));
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.api.impl.CountyGenerateServiceImpl#updateData()}.
	 */ 
	@Test
	void testUpdateData() throws ApiException
	{
		assertNotNull(countyGenerateServiceimpl.updateData("test-data", metadata));
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.api.impl.CountyGenerateServiceImpl#getEndpointData()}.
	 */ 
	@Test
	void testGetEndpointData() 
	{  
		String actual=countyGenerateServiceimpl.getEndpointData("APIGEE_API_KEY");
		assertEquals("APIGEE_API_KEY", actual);
	}

}
