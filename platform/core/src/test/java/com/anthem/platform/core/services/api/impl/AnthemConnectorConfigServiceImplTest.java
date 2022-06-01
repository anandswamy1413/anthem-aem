package com.anthem.platform.core.services.api.impl;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.anthem.platform.core.services.endpoints.configs.ApigeeEndpointConfig;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnthemConnectorConfigServiceImplTest {

	@InjectMocks
	private AnthemConnectorConfigServiceImpl anthemconnectorconfigserviceimpl;

	@Mock
	private ApigeeEndpointConfig config;
	
	@BeforeEach
	void setUp() throws Exception
	{
	   MockitoAnnotations.initMocks(this);
	   when(config.connectionTimeOut()).thenReturn(1000);
	   when(config.socketTimeOut()).thenReturn(1000);
	   when(config.requestTimeOut()).thenReturn(1000);
	   anthemconnectorconfigserviceimpl.activate(config);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.api.impl.AnthemConnectorConfigServiceImpl#getConnectionTimeOut()}.
	 */
	@Test
	void testGetConnectionTimeOut() 
	{
	   int expected=1000;
	   assertEquals(expected,anthemconnectorconfigserviceimpl.getConnectionTimeOut());
	}


	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.api.impl.AnthemConnectorConfigServiceImpl#getSocketTimeOut()}.
	 */
	@Test
	void testGetSocketTimeOut()
	{
		int expected=1000;
		assertEquals(expected,anthemconnectorconfigserviceimpl.getSocketTimeOut());
	}
   

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.api.impl.AnthemConnectorConfigServiceImpl#getRequestTimeOut()}.
	 */
	@Test
	void testGetRequestTimeOut() 
	{
		int expected=1000;
	    assertEquals(expected,anthemconnectorconfigserviceimpl.getRequestTimeOut());
	}

}
