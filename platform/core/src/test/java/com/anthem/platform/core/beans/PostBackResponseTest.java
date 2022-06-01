package com.anthem.platform.core.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

class PostBackResponseTest {

	private static final int HTTP_STATUS_CODE = 200;

	Map<String, Object> success = new HashMap<>();
	
	Map<String, Object> error = new HashMap<>();
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetStatus() {
		PostBackResponse postResponse = new PostBackResponse(PostBackResponse.Status.SUCCESS, HTTP_STATUS_CODE);
		String actual = "SUCCESS";
		postResponse.setStatus("SUCCESS");
		String expected = postResponse.getStatus();
		assertEquals(expected, actual);
		assertNotNull(postResponse);
		
	}
	
	@Test
	void testAddErrorCode() {
		PostBackResponse postResponse = new PostBackResponse(PostBackResponse.Status.SUCCESS, HTTP_STATUS_CODE);
		postResponse.addErrorCode("500");
		assertNotNull(postResponse);
	}
	
	@Test
	void testAddErrorMsg() {
		PostBackResponse postResponse = new PostBackResponse(PostBackResponse.Status.SUCCESS, HTTP_STATUS_CODE);
		postResponse.addErrorMsg("Internal Server Error");
		assertNotNull(postResponse);
	}
	
	@Test
	void testGetErrorData() {
		PostBackResponse postResponse = new PostBackResponse(PostBackResponse.Status.SUCCESS, HTTP_STATUS_CODE);
		Map<String, Object> actual = new HashMap<>();
		postResponse.setErrorData(error);
		Map<String, Object> expected = postResponse.getErrorData();
		assertEquals(expected, actual);
		assertNotNull(postResponse);
	}
	
	@Test
	void testGetSuccessData() {
		PostBackResponse postResponse = new PostBackResponse(PostBackResponse.Status.SUCCESS, HTTP_STATUS_CODE);
		Map<String, Object> actual = new HashMap<>();
		postResponse.setSuccessData(success);
		Map<String, Object> expected = postResponse.getSuccessData();
		assertEquals(expected, actual);
		assertNotNull(postResponse);
	}
	
	@Test
	void testGetHttpStatusCode() {
		PostBackResponse postResponse = new PostBackResponse(PostBackResponse.Status.SUCCESS, HTTP_STATUS_CODE);
		int actual = 500;
		postResponse.setHttpStatusCode(500);
		int expected = postResponse.getHttpStatusCode();
		assertEquals(expected, actual);
		assertNotNull(postResponse);
	}
}
