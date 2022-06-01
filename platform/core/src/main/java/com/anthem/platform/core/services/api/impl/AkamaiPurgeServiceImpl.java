package com.anthem.platform.core.services.api.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.akamai.edgegrid.signer.ClientCredential;
import com.akamai.edgegrid.signer.apachehttpclient.ApacheHttpClientEdgeGridInterceptor;
import com.akamai.edgegrid.signer.apachehttpclient.ApacheHttpClientEdgeGridRoutePlanner;
import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.beans.PostBackResponse;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.api.ApiService;
import com.anthem.platform.core.services.api.configs.AkamaiFlushServiceConfig;
import com.anthem.platform.core.services.endpoints.ApiEndpointFactory;
import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.utils.PlatformHelper;

@Designate(ocd = AkamaiFlushServiceConfig.class)
@Component(service = ApiService.class, immediate = true, property = {
		"apiId=" + AkamaiPurgeServiceImpl.API_ID }, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class AkamaiPurgeServiceImpl implements ApiService<String, String> {

	@Reference
	private JacksonMapperService jacksonMapper;

	@Reference
	private ApiEndpointFactory apiEndpointFactory;

	@Reference
	private PlatformHelper platformHelper;

	private ApiEndpointService apiEndpointService;

	private String akamaiHost;
	private String cacheInvalidatePath;
	private String cacheDeltePath;

	public static final String API_ID = "Akamai cache purge";
	public static final String API_DESC = "Service to flush out cache from akamai";
	public static final String ERROR_CODE_UPDATE_DATA = "API:REST:AKAMAI_CACHE_PURGE_FAILED";

	private static final String START_REQUEST = "*************** Akamai Purge ************* Start Request ********************* ";
	private static final String TOTAL_TIME_TAKEN_FOR_API_REQUEST_MS = "Total Time Taken for Akamai Purge API Request - {} ms";
	private static final String END_REQUEST = "****************** Akamai Purge ********** End Request *********************";
	private static final String EXIT_SEND_REQUEST_WITH_BODY = "Exit sendRequestWithBody";

	private static final Logger LOG = LoggerFactory.getLogger(AkamaiPurgeServiceImpl.class);

	@Activate
	@Modified
	public void activate(AkamaiFlushServiceConfig config) {
		String endpointId = config.endpointId();
		this.apiEndpointService = apiEndpointFactory.getEndpoint(endpointId);
		this.akamaiHost = this.apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_HOST_URL.name());
		this.cacheDeltePath = config.cacheDeletePath();
		this.cacheInvalidatePath = config.cacheInvalidatePath();
	}

	@Override
	public String getApiId() {
		return API_ID;
	}

	@Override
	public String getDesc() {
		return API_DESC;
	}

	@Override
	public String getResults(String apiData, Map<String, Object> metadata) throws ApiException {
		String path = StringUtils.EMPTY;
		if (apiData.equals("invalidate"))
			path = cacheInvalidatePath;
		if (apiData.equals("delete"))
			path = cacheDeltePath;
		LOG.info("Request Body map is {}", metadata);
		long startTime = platformHelper.getStartTime();
		LOG.info(START_REQUEST);
		ClientCredential clientCredential = ClientCredential.builder()
				.accessToken(this.apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_ACCESS_TOKEN.name()))
				.clientToken(this.apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_CLIENT_TOKEN.name()))
				.clientSecret(this.apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_CLIENT_SECRET.name()))
				.host(akamaiHost).build();
		LOG.info("ClientCredential for akamai purge {}", clientCredential);
		int conTimeout = Integer
				.parseInt(this.apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_CONNECTION_TIMEOUT.name()));
		int socTimeout = Integer
				.parseInt(this.apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_SOCKET_TIMEOUT.name()));
		int reqTimeout = Integer
				.parseInt(this.apiEndpointService.get(EndpointKeys.AkamaiEndpointKey.AKAMAI_REQUEST_TIMEOUT.name()));
		RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(conTimeout * 1000)
				.setConnectionRequestTimeout(reqTimeout * 1000).setSocketTimeout(socTimeout * 1000).build();
		HttpClient client = HttpClientBuilder.create()
				.addInterceptorFirst(new ApacheHttpClientEdgeGridInterceptor(clientCredential))
				.setRoutePlanner(new ApacheHttpClientEdgeGridRoutePlanner(clientCredential))
				.setDefaultRequestConfig(requestConfig).build();
		HttpPost request = new HttpPost(PlatformConstants.HTTPS_COLON.concat(akamaiHost).concat(path));
		String reqBody = jacksonMapper.mapToJson(metadata);
		StringEntity requestEntity = new StringEntity(reqBody, ContentType.APPLICATION_JSON);
		LOG.info("Request Body is {} and Request Entity for akamai purge is {}", reqBody, requestEntity);
		request.setEntity(requestEntity);
		LOG.info(EXIT_SEND_REQUEST_WITH_BODY);
		try {
			APIResponse response = platformHelper.getResponse(client.execute(request));
			if (response.getStatusCode() == 201) {
				LOG.debug(TOTAL_TIME_TAKEN_FOR_API_REQUEST_MS, platformHelper.logMethodTime(startTime));
				PostBackResponse postBackResponse = platformHelper.getSuccessPostBackResponse(response.getStatusCode(),
						jacksonMapper.convertJsonToMap(response.getResponse()));
				Map<String, Object> respMap = new HashMap<>();
				respMap.put("httpStatus",
						Integer.parseInt(postBackResponse.getSuccessData().get("httpStatus").toString()));
				respMap.put("purgeId", postBackResponse.getSuccessData().get("purgeId"));
				LOG.debug(END_REQUEST);
				return jacksonMapper.mapToJson(respMap);
			}
		} catch (IOException e) {
			LOG.error("Exception while purging akamai ", e);
		}
		return StringUtils.EMPTY;
	}

	@Override
	public String updateData(String apiData, Map<String, Object> metadata) throws ApiException {
		return StringUtils.EMPTY;
	}

	@Override
	public String getEndpointData(String key) {
		return StringUtils.EMPTY;
	}

}
