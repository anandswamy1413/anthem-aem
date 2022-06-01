package com.anthem.platform.core.services.endpoints.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.endpoints.configs.SiebelEndpointConfig;

class SiebelEndpointServiceImplTest {

	@InjectMocks
	private SiebelEndpointServiceImpl siebelEndpointServiceImpl;
	
	@Mock
	private SiebelEndpointConfig config;
	
	private Map<String,String> expected;
	
	@BeforeEach
	void setUp() throws Exception
	{MockitoAnnotations.initMocks(this);
	  expected=new HashMap();
	  
       when(config.endpointId()).thenReturn("test_apigee");
       when(config.siebelHostUrl()).thenReturn("https://123.32.120.68");
       when(config.enableDebugLogsForSoap()).thenReturn(true);
       when(config.routingCode()).thenReturn("11");
       when(config.vendorSource()).thenReturn("TEST");
       expected.put(EndpointKeys.SiebelEndpointKey.SIEBEL_HOST_URL.name(),"https://123.32.120.68");
       expected.put(EndpointKeys.SiebelEndpointKey.SOAP_DEBUG_ENABLED.name(),"true");
       expected.put(EndpointKeys.SiebelEndpointKey.ROUTING_CODE.name(),"11");
       expected.put(EndpointKeys.SiebelEndpointKey.VENDOR_SOURCE.name(),"TEST");
	  siebelEndpointServiceImpl.activate(config);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.endpoints.impl.SiebelEndpointServiceImpl#getEndpointConfigs()}.
	 */		
	@Test
	void testGetEndpointConfigs() {
		 Map<String,String> actual=siebelEndpointServiceImpl.getEndpointConfigs();
		 assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.endpoints.impl.SiebelEndpointServiceImpl#get()}.
	 */		
	@Test
	void testGet()
	{
	  String actual=siebelEndpointServiceImpl.get("SIEBEL_HOST_URL");
	  final String expected="https://123.32.120.68";
	  assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.endpoints.impl.SiebelEndpointServiceImpl#add()}.
	 */		
	@Test
	void testAdd()
	{
	  siebelEndpointServiceImpl.add("API_NEW_KEY","NewKeyValue");
		assertNotNull(siebelEndpointServiceImpl);

	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.endpoints.impl.SiebelEndpointServiceImpl#getEndpointId()}.
	 */		
	@Test
	void testGetEndpointId()
	{
	  String actual=siebelEndpointServiceImpl.getEndpointId();
	  final String expected="test_apigee";
	  assertEquals(expected, actual);
	}

}
