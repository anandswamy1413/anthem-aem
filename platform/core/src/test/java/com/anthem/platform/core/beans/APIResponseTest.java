package com.anthem.platform.core.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class APIResponseTest {

	private static final String RESPONSE = "response";
	private static final int STATUS_CODE = 200;
	
	@InjectMocks
	APIResponse apiResponse;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		apiResponse.setResponse(RESPONSE);
		apiResponse.setStatusCode(STATUS_CODE);
	}

	@Test
	void testGetResponse() {
		String actual = "response";
		String expected = apiResponse.getResponse();
		assertNotNull(apiResponse);
		assertEquals(expected, actual);
		
	}
	
	@Test
	void testGetStatusCode() {
		int actual = 200;
		int expected = apiResponse.getStatusCode();
		assertNotNull(apiResponse);
		assertEquals(expected, actual);
	}
}
