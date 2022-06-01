package com.anthem.platform.core.beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

class OAuthRequestTest {

	private static final String ACCOUNT_ID = "account id";
	private static final String GRANT_TYPE = "grant type";
	private static final String CLIENT_SECRET = "client secret";
	private static final String CLIENT_ID = "client id";

	@Mock
	private Logger log;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testGetClientId() throws ApiException {
		OAuthRequest oauthRequest = new OAuthRequest(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE, ACCOUNT_ID);
		String actual = "client id";
		String expected = oauthRequest.getClientId();
		assertEquals(expected, actual);

	}

	@Test
	void testGetClientSecret() throws ApiException {
		OAuthRequest oauthRequest = new OAuthRequest(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE, ACCOUNT_ID);
		String actual = "client secret";
		String expected = oauthRequest.getClientSecret();
		assertEquals(expected, actual);
	}

	@Test
	void testGetGrantType() throws ApiException {
		OAuthRequest oauthRequest = new OAuthRequest(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE, ACCOUNT_ID);
		String actual = "grant type";
		String expected = oauthRequest.getGrantType();
		assertEquals(expected, actual);
	}

	@Test
	void testGetAccountId() throws ApiException {
		OAuthRequest oauthRequest = new OAuthRequest(CLIENT_ID, CLIENT_SECRET, GRANT_TYPE, ACCOUNT_ID);
		String actual = "account id";
		String expected = oauthRequest.getAccountId();
		assertEquals(expected, actual);
	}

	@Test
	void testGetGrantTypeWhenBlank() throws ApiException {
		try {
			OAuthRequest oauthRequest = new OAuthRequest(CLIENT_ID, CLIENT_SECRET, StringUtils.EMPTY,
					StringUtils.EMPTY);
			oauthRequest.getGrantType();
			assertNotNull(oauthRequest);
		} catch (ApiException e) {
			log.error("Api Exception occured");
		}
	}
}
