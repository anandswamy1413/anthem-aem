package com.anthem.platform.core.services.api.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.akamai.edgegrid.signer.ClientCredential;
import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.api.configs.AkamaiFlushServiceConfig;
import com.anthem.platform.core.services.endpoints.ApiEndpointFactory;
import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.utils.PlatformHelper;

class AkamaiPurgeServiceImplTest {

	@InjectMocks
	AkamaiPurgeServiceImpl akamaiPurgeServiceImpl;

	@Mock
	JacksonMapperService jacksonMapper;

	@Mock
	ApiEndpointFactory apiEndpointFactory;

	@Mock
	PlatformHelper platformHelper;

	@Mock
	ApiEndpointService apiEndpointService;

	@Mock
	AkamaiFlushServiceConfig config;

	@Mock
	ClientCredential clientCredential;

	@Mock
	HttpClient httppClient;

	@Mock
	PlatformConstants platformConstants;

	@Mock
	StringEntity StringEntity;

	@Mock
	HttpPost httpPost;
	
	@Mock
	APIResponse apiResponse;
	
	@Mock
	RequestConfig requestConfig;

	private String akamaiHost = "http://localhost:4200";
	private String cacheInvalidatePath = "invalidpath";
	private String cacheDeltePath = "deleted path";

	public static final String API_ID = "Akamai cache purge";
	public static final String API_DESC = "Service to flush out cache from akamai";
	public String apiData = "apiData";
	String str = PlatformConstants.HTTPS_COLON.concat(akamaiHost).concat("/invalidpath");

	Map<String, Object> metadata = new HashMap<>();

	@BeforeEach
	void setUp() throws ClientProtocolException, IOException {
		MockitoAnnotations.initMocks(this);
		metadata.put("key", "value");
		when(config.endpointId()).thenReturn("endpointId");
		when(apiEndpointFactory.getEndpoint("endpointId")).thenReturn(apiEndpointService);
		when(apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_HOST_URL.name())).thenReturn("akamaiHost");
		when(config.cacheDeletePath()).thenReturn(cacheDeltePath);
		when(config.cacheInvalidatePath()).thenReturn(cacheInvalidatePath);
		when(platformHelper.getStartTime()).thenReturn((long) 5343434);
		when(apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_ACCESS_TOKEN.name()))
				.thenReturn("akamai_access_token");
		when(apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_CLIENT_TOKEN.name()))
				.thenReturn("akamai_client_token");
		when(apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_CLIENT_SECRET.name()))
				.thenReturn("akamai_client_secret");
		when(apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_CONNECTION_TIMEOUT.name()))
		.thenReturn("60");
		when(apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_SOCKET_TIMEOUT.name()))
		.thenReturn("60");
		when(apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_REQUEST_TIMEOUT.name()))
		.thenReturn("60");
		when(jacksonMapper.mapToJson(metadata)).thenReturn("jacksonMapper");
		when(platformHelper.getResponse(httppClient.execute(httpPost))).thenReturn(apiResponse);

	}

	@Test
	void testGetResults() throws ApiException {
		assertNotNull(akamaiPurgeServiceImpl);
		akamaiPurgeServiceImpl.activate(config);
		akamaiPurgeServiceImpl.getResults("invalidate", metadata);
	}

	@Test
	void testGetApiId() {
		String apiId = akamaiPurgeServiceImpl.getApiId();
		assertEquals(API_ID, apiId);
	}

	@Test
	void testGetDesc() {
		String apiDesc = akamaiPurgeServiceImpl.getDesc();
		assertEquals(API_DESC, apiDesc);
	}

	@Test
	void testUpdateData() throws ApiException {
		String empty = akamaiPurgeServiceImpl.updateData("apiData", metadata);
		assertEquals(StringUtils.EMPTY, empty);
	}

	@Test
	void testGetEndpointData() {
		String empty = akamaiPurgeServiceImpl.getEndpointData("key");
		assertEquals(StringUtils.EMPTY, empty);
	}
}
