package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.services.utility.GenericListService;

class EndpointUrlTest {

	@InjectMocks
	EndpointUrl endpointUrl;
	
	@Mock
	private Map<String,String> endpointMap;
	
	@Mock
	private Map<String,String> cpCodesMap;
	
	@Mock
	private GenericListService genericListService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testInit() throws Exception {
		endpointUrl.init();
		assertNotNull(endpointUrl);
	}
	
	@Test
	void testGetEndpointMap() {
		Map<String, String> actual = endpointUrl.getEndpointMap();
		endpointUrl.getCpCodesMap();
		assertNotNull(actual);
	}
	
	@Test
	void testGetCpCodesMap() {
		Map<String, String> actual = endpointUrl.getCpCodesMap();
		endpointUrl.getCpCodesMap();
		assertNotNull(actual);
		
	}
}
