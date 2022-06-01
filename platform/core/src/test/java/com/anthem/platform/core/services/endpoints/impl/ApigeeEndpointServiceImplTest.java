package com.anthem.platform.core.services.endpoints.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.endpoints.configs.ApigeeEndpointConfig;

class ApigeeEndpointServiceImplTest {

	@InjectMocks
	private ApigeeEndpointServiceImpl apigeEndpointServiceImpl=new ApigeeEndpointServiceImpl();
	
	@Mock
	private ApigeeEndpointConfig config;
	
	private Map<String, String> expected;

	@BeforeEach
	void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		expected=new HashMap();
	  
	    when(config.endpointId()).thenReturn("test_apigee");
	    when(config.apigeeApiKey()).thenReturn("testkey");
	    when(config.apigeeHostUrl()).thenReturn("https://test.com");
	    when(config.apigeeClientID()).thenReturn("000aaa");
	    when(config.apigeeClientSecretKey()).thenReturn("0000aaaa1111");
		
		  expected.put(EndpointKeys.ApigeeEndpointKey.APIGEE_API_KEY.name(),"testkey");
		  expected.put(EndpointKeys.ApigeeEndpointKey.APIGEE_HOST_URL.name(),"https://test.com");
		  expected.put(EndpointKeys.ApigeeEndpointKey.API_CLIENT_ID.name(),"000aaa");
		  expected.put(EndpointKeys.ApigeeEndpointKey.API_CLIENT_SECRET.name(),"0000aaaa1111");
		  apigeEndpointServiceImpl.activate(config);	
		 
	}


	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.endpoints.impl.ApigeeEndpointServiceImpl#getEndpointConfigs()}.
	 */
	@Test
	void testGetEndpointConfigs()
	{
	   Map<String,String> actual=apigeEndpointServiceImpl.getEndpointConfigs();
	   assertEquals(expected, actual);
	   
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.endpoints.impl.ApigeeEndpointServiceImpl#get()}.
	 */	
	  @Test
	  void testGet()
	  {
		 String actual= apigeEndpointServiceImpl.get("APIGEE_API_KEY");
		 final String expected="testkey";
		 assertEquals(expected, actual);
	  }
	  
	  
	  /**
		 * Test method for
		 * {@link com.anthem.platform.core.services.endpoints.impl.ApigeeEndpointServiceImpl#add()}.
		 */	
	  @Test
	  void testAdd()
	  {  
		  apigeEndpointServiceImpl.add("API_NEW_KEY","NewKeyValue");
		  assertNotNull(apigeEndpointServiceImpl);
		 	
	  }
	  
	  /**
		 * Test method for
		 * {@link com.anthem.platform.core.services.endpoints.impl.ApigeeEndpointServiceImpl#getEndpointId()}.
		 */	
	  @Test
	  void testGetEndpointId() 
	  {
		String actual= apigeEndpointServiceImpl.getEndpointId();
		final String expected="test_apigee";
		assertEquals(expected, actual);
	  }
	 
}
