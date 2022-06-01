package com.anthem.platform.core.services.endpoints.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.endpoints.configs.SFMCEndpointConfig;

class SFMCEndpointServiceImplTest {

	@InjectMocks
	private SFMCEndpointServiceImpl sfmcEndpointServiceImpl;

	@Mock
	private SFMCEndpointConfig config;

	private Map<String, String> expected;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		expected = new HashMap();
		when(config.endpointId()).thenReturn("test_apigee");
		when(config.accountId()).thenReturn("10000");
		when(config.authUrl()).thenReturn("https://test.com");
		when(config.clientId()).thenReturn("abcd1234");
		when(config.clientSecret()).thenReturn("0000aaaa");
		when(config.grantType()).thenReturn("test_credentials");
		when(config.triggerMailAPIPath()).thenReturn("test/key:2694/send");
		expected.put(EndpointKeys.SFMCEndpointKey.ACCOUNT_ID.name(), "10000");
		expected.put(EndpointKeys.SFMCEndpointKey.AUTH_URL.name(), "https://test.com");
		expected.put(EndpointKeys.SFMCEndpointKey.CLIENT_ID.name(), "abcd1234");
		expected.put(EndpointKeys.SFMCEndpointKey.CLIENT_SECRET.name(), "0000aaaa");
		expected.put(EndpointKeys.SFMCEndpointKey.GRANT_TYPE.name(), "test_credentials");
		expected.put(EndpointKeys.SFMCEndpointKey.TRIGGER_MAIL_API_PATH.name(), "test/key:2694/send");
		expected.put(EndpointKeys.SFMCEndpointKey.SUBSCRIBE_API_PATH.name(), null);
		sfmcEndpointServiceImpl.activate(config);
	}

	@Test
	void testGetEndpointConfigs() {
		Map<String, String> actual = sfmcEndpointServiceImpl.getEndpointConfigs();
		assertEquals(expected, actual);
	}

	@Test
	void testGet() {
		String actual = sfmcEndpointServiceImpl.get("ACCOUNT_ID");
		final String exepected = "10000";
		assertEquals(exepected, actual);
	}

	@Test
	void testAdd() {
		sfmcEndpointServiceImpl.add("API_NEW_KEY", "NewKeyValue");
		assertNotNull(sfmcEndpointServiceImpl);
	}

	@Test
	void testGetEndpointId() {
		String actual = sfmcEndpointServiceImpl.getEndpointId();
		final String expected = "test_apigee";
		assertEquals(expected, actual);
	}

}
