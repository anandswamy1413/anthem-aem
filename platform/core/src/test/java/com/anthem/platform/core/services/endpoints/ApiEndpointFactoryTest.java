package com.anthem.platform.core.services.endpoints;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ApiEndpointFactoryTest {
	
	@Mock
	private Map<String, ApiEndpointService> endpointMap = new HashMap<>();
	
	@InjectMocks
	ApiEndpointFactory apiEndpointFactory;
	
	@Mock
	ApiEndpointService apiEndpointService;

	@BeforeEach
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		
	}

	@Test
	void testGetEndpointMap() {
		Map<String, ApiEndpointService>  actual=apiEndpointFactory.getEndpointMap();
		assertEquals(endpointMap,actual);
	}
	
	@Test
	void testGetEndpoint() {
		when(endpointMap.getOrDefault("endpointId", null)).thenReturn(apiEndpointService);
		ApiEndpointService  actual=apiEndpointFactory.getEndpoint("endpointId");
		assertEquals(apiEndpointService,actual);
	}
	
	@Test
	void testBind() {
		when(apiEndpointService.getEndpointId()).thenReturn("endpointId");
		apiEndpointFactory.bind(apiEndpointService);
		assertNotNull(apiEndpointFactory);
	}	

	@Test
	void testUnbind() {
		when(apiEndpointService.getEndpointId()).thenReturn("endpointId");
		apiEndpointFactory.unbind(apiEndpointService);
		assertNotNull(apiEndpointFactory);
	}
	

}
