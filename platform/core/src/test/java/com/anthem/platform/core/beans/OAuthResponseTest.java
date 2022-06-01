package com.anthem.platform.core.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

class OAuthResponseTest {

	private static final String ACCESS_TOKEN = "access token";
	private static final String TOKEN_TYPE = "token type";
	private static final int EXPIRES_IN = 2000;
	private static final String SCOPE = "scope";
	private static final String SOAP_INSTANCE_URL = "/path/to/soap_instance/url";
	private static final String REST_INSTANCE_URL = "/path/to/rest_instance/url";
	private static final String ISSUED_AT = "20 sep 2020";
	private static final String APPLICATION_NAME = "app name";
	private static final int STATUS = 200;
	private static final String CLIENT_ID = "client id";
	
	@InjectMocks
	OAuthResponse oauthResp;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		oauthResp.setAccessToken(ACCESS_TOKEN);
		oauthResp.setTokenType(TOKEN_TYPE);
		oauthResp.setExpiresIn(EXPIRES_IN);
		oauthResp.setScope(SCOPE);
		oauthResp.setSoapInstanceUrl(SOAP_INSTANCE_URL);
		oauthResp.setRestInstanceUrl(REST_INSTANCE_URL);
		oauthResp.setIssuedAt(ISSUED_AT);
		oauthResp.setApplicationName(APPLICATION_NAME);
		oauthResp.setStatusCode(STATUS);
		oauthResp.setClientId(CLIENT_ID);
	}

	@Test
	void testGetClientId() {
		String actual = "client id";
		String expected = oauthResp.getClientId();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
		
	}
	
	@Test
	void testGetStatusCode() {
		int actual = 200;
		int expected = oauthResp.getStatusCode();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetIssuedAt() {
		String actual = "20 sep 2020";
		String expected = oauthResp.getIssuedAt();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetApplicationName() {
		String actual = "app name";
		String expected = oauthResp.getApplicationName();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetRestInstanceURL() {
		String actual = "/path/to/rest_instance/url";
		String expected = oauthResp.getRestInstanceUrl();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetSoapInstanceURL() {
		String actual = "/path/to/soap_instance/url";
		String expected = oauthResp.getSoapInstanceUrl();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetScope() {
		String actual = "scope";
		String expected = oauthResp.getScope();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetExpiresIn() {
		int actual = 2000;
		int expected = oauthResp.getExpiresIn();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetTokenType() {
		String actual = "token type";
		String expected = oauthResp.getTokenType();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetAccessToken() {
		String actual = "access token";
		String expected = oauthResp.getAccessToken();
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetStatus() {
		String actual = "success";
		oauthResp.setStatus("success");
		String expected = oauthResp.getStatus();		
		assertNotNull(oauthResp);
		assertEquals(expected, actual);
	}
}
