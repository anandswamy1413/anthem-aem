package com.anthem.platform.core.services.api.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.http.MethodNotSupportedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.beans.BaseRequest;
import com.anthem.platform.core.beans.OAuthRequest;
import com.anthem.platform.core.beans.OAuthResponse;
import com.anthem.platform.core.services.api.configs.SFMCConnectorServiceConfig;
import com.anthem.platform.core.services.endpoints.ApiEndpointFactory;
import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.services.utility.RestClient;
import com.anthem.platform.core.services.utility.impl.RestClientImpl;
import com.google.gson.Gson;

class SFMCConnectorServiceImplTest {

	@InjectMocks
	private SFMCConnectorServiceImpl sfmcConnectorServiceimpl;
	
	@Mock
	private ApiEndpointService apiEndpointService;

	@Mock

	private RestClient restClient=Mockito.mock(RestClientImpl.class);


	@Mock
	private  ApiEndpointFactory apiEndpointFactory;
	
    @Mock
    private JacksonMapperService jacksonMapper;

    @Mock
    private SFMCConnectorServiceConfig config;
	
    @Mock

    private OAuthRequest oAuthRequest=Mockito.mock(OAuthRequest.class);

    @InjectMocks
	private Gson gson = new Gson();
	 
    @Mock
    private BaseRequest request=Mockito.mock(BaseRequest.class);
    
    @Mock
    private APIResponse apiResponse=Mockito.mock(APIResponse.class);
    
    @Mock
    private OAuthResponse oAuthResponse;
    
    @BeforeEach
    void setUp() throws Exception 
	{
		MockitoAnnotations.initMocks(this);
		Gson gson = Mockito.mock(Gson.class);
		when(config.endpointId()).thenReturn("test_id");
		when(apiEndpointFactory.getEndpoint("test_id")).thenReturn(apiEndpointService);
		sfmcConnectorServiceimpl.activate(config);
		when(sfmcConnectorServiceimpl.getEndpointData("test_id")).thenReturn("test_value");
		when(gson.toJson(oAuthRequest)).thenReturn("{\"brand\":\"Jeep\", \"doors\": 3}");
		when(restClient.sendRequest(Mockito.any(BaseRequest.class),Mockito.anyMap())).thenReturn(apiResponse);	
	    when(apiResponse.getStatusCode()).thenReturn(400);
	    when(restClient.sendRequest(Mockito.any(BaseRequest.class),Mockito.eq(null))).thenReturn(apiResponse);
	    when(apiResponse.getStatusCode()).thenReturn(200);
		when(apiResponse.getResponse()).thenReturn("Response");
		when(jacksonMapper.convertJsonToObject(apiResponse.getResponse(), OAuthResponse.class)).thenReturn(oAuthResponse);
	
	}

	
	  @Test
	  void testGetAuthTokenResponse() throws MethodNotSupportedException, IOException, ApiException
	  { OAuthRequest oAuthRequest=new OAuthRequest("clientid", "clientSecret", "grantType", "accountId");
	  sfmcConnectorServiceimpl.getAuthTokenResponse(oAuthRequest,"com/anthem/platform");
	   assertNotNull(sfmcConnectorServiceimpl);
	   }
	 
	 
	@Test
	void testTriggerEmail() throws MethodNotSupportedException, IOException
    {
		assertEquals(apiResponse,sfmcConnectorServiceimpl.triggerEmail("sample_token", "com/anthem/platform","test"));
	}

	@Test
	void testGetEndpointData()
	{
	    String actual=sfmcConnectorServiceimpl.getEndpointData("test_id");
		assertEquals("test_value", actual);
	}

}
