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
import com.anthem.platform.core.services.endpoints.configs.AkamaiEndPointServiceConfig;

class AkamaiEndpointServiceImplTest {

	@InjectMocks
	private AkamaiEndpointServiceImpl akamaiEndpointServiceImpl = new AkamaiEndpointServiceImpl();

	@Mock
	private AkamaiEndPointServiceConfig config;

	Map<String, String> expected = new HashMap<String, String>();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(config.endpointId()).thenReturn("test_akamai");
		when(config.akamaiAccessToken()).thenReturn("akamai_access_token");
		when(config.akamaiClientSecretKey()).thenReturn("akamai_client_secret");
		when(config.akamaiClientToken()).thenReturn("akamai_client_token");
		when(config.akamaiHostUrl()).thenReturn("https://akamai.com");
		when(config.connectionTimeOut()).thenReturn("60");
		when(config.socketTimeOut()).thenReturn("60");
		when(config.requestTimeOut()).thenReturn("60");

		expected.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_ACCESS_TOKEN.name(), "test_access_key");
		expected.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_HOST_URL.name(), "https://akamai.com");
		expected.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_CLIENT_TOKEN.name(), "000aaa");
		expected.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_CLIENT_SECRET.name(), "0000aaaa1111");
		expected.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_CONNECTION_TIMEOUT.name(), "60");
		expected.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_SOCKET_TIMEOUT.name(), "60");
		expected.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_REQUEST_TIMEOUT.name(), "60");
		akamaiEndpointServiceImpl.activate(config);
	}

	@Test
	void testGet() {
		String actual = akamaiEndpointServiceImpl.get("AKAMAI_ACCESS_TOKEN");
		final String expected = "akamai_access_token";
		assertEquals(expected, actual);
	}

	@Test
	void testAdd() {
		akamaiEndpointServiceImpl.add("AKAMAI_NEW_KEY", "NewKeyValue");
		assertNotNull(akamaiEndpointServiceImpl);

	}

	@Test
	void testGetEndpointId() {
		String actual = akamaiEndpointServiceImpl.getEndpointId();
		final String expected = "test_akamai";
		assertEquals(expected, actual);
	}

}
