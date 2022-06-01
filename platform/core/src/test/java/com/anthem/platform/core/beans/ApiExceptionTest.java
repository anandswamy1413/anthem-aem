package com.anthem.platform.core.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class ApiExceptionTest {

	private static final String ERROR_CODE = "error code";
	private static final String ERROR_MSG = "error message";
	
	@InjectMocks
	ApiException apiExe;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetErrorCode() {
		ApiException apiException = new ApiException(ERROR_CODE, ERROR_MSG);
		String actual = "error code";
		String expected = apiException.getErrorCode();
		assertEquals(expected, actual);
		
	}
	
	@Test
	void testGetErrorMsg() {
		ApiException apiException = new ApiException(ERROR_CODE, ERROR_MSG);
		String actual = "error message";
		String expected = apiException.getErrorMsg();
		assertEquals(expected, actual);
	}
	
}
