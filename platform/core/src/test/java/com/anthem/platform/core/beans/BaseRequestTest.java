package com.anthem.platform.core.beans;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class BaseRequestTest {

	private static final String URL = "/path/to/request/url";
	private static final String DATA = "Sample data";
	
	@InjectMocks
	BaseRequest baseRequest;
	
	@Mock
	private HttpMethodType method;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testConstructerWithTwoParams() {
		BaseRequest baseReq = new BaseRequest(URL, HttpMethodType.GET);
		baseReq.gethttpMethodType();
		baseReq.getMethod();
		baseReq.getUrl();
		baseReq.getData();
		baseReq.getRequestId();
		assertNotNull(baseReq);
	}
	
	@Test
	void testConstructerWithThreeParams() {
		BaseRequest baseReq = new BaseRequest(URL, HttpMethodType.GET, DATA);
		baseReq.gethttpMethodType();
		baseReq.getMethod();
		baseReq.getUrl();
		baseReq.getData();
		baseReq.getRequestId();
		assertNotNull(baseReq);
	}
	
	@Test
	void testHttpMethodPost() {
		BaseRequest baseReq = new BaseRequest(URL, HttpMethodType.POST, DATA);
		baseReq.gethttpMethodType();
		baseReq.getMethod();
		assertNotNull(baseReq);
	}
	
	@Test
	void testHttpMethodPut() {
		BaseRequest baseReq = new BaseRequest(URL, HttpMethodType.PUT, DATA);
		baseReq.gethttpMethodType();
		baseReq.getMethod();
		assertNotNull(baseReq);
	}
	
	@Test
	void testHttpMethodDelete() {
		BaseRequest baseReq = new BaseRequest(URL, HttpMethodType.DELETE, DATA);
		baseReq.gethttpMethodType();
		baseReq.getMethod();
		assertNotNull(baseReq);
	}
	
	@Test
	void testHttpMethodDefault() {
		BaseRequest baseReq = new BaseRequest(URL, HttpMethodType.PATCH, DATA);
		baseReq.gethttpMethodType();
		baseReq.getMethod();
		assertNotNull(baseReq);
	}
}