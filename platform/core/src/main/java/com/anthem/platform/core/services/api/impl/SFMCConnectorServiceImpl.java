package com.anthem.platform.core.services.api.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.MethodNotSupportedException;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.engine.SlingRequestProcessor;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.beans.BaseRequest;
import com.anthem.platform.core.beans.HttpMethodType;
import com.anthem.platform.core.beans.OAuthRequest;
import com.anthem.platform.core.beans.OAuthResponse;
import com.anthem.platform.core.services.api.SFMCConnectorService;
import com.anthem.platform.core.services.api.configs.SFMCConnectorServiceConfig;
import com.anthem.platform.core.services.endpoints.ApiEndpointFactory;
import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.services.utility.RestClient;
import com.day.cq.contentsync.handler.util.RequestResponseFactory;
import com.google.gson.Gson;

@Component(service = SFMCConnectorService.class, immediate = true)
@Designate(ocd = SFMCConnectorServiceConfig.class)
public class SFMCConnectorServiceImpl implements SFMCConnectorService {

	private static final Logger LOG = LoggerFactory.getLogger(SFMCConnectorServiceImpl.class);

	private ApiEndpointService apiEndpointService;

	@Reference
	private RestClient restClient;

	@Reference
	private  ApiEndpointFactory apiEndpointFactory;

	@Reference
	private  ResourceResolverFactory resourceResolverFactory;

	@Reference
	private  RequestResponseFactory requestResponseFactory;

	@Reference
	private  SlingRequestProcessor slingRequestProcessor;
	
	
    @Reference
    private JacksonMapperService jacksonMapper;

	
	@Activate
	@Modified
	public void activate(SFMCConnectorServiceConfig config) {
		String endpointId = config.endpointId();
		LOG.info("Reading endpoint details from {} ", endpointId);
		this.apiEndpointService = apiEndpointFactory.getEndpoint(endpointId);
	}


	@Override
	public OAuthResponse getAuthTokenResponse(OAuthRequest oAuthRequest, String authUrl)
			throws ApiException, MethodNotSupportedException, IOException {

		Gson gson = new Gson();
		String requestJson = gson.toJson(oAuthRequest);
		BaseRequest request = new BaseRequest(authUrl, HttpMethodType.POST, requestJson);

		APIResponse apiResponse = restClient.sendRequest(request, null);

		if (apiResponse.getStatusCode() > 300) {
			throw new ApiException(String.valueOf(apiResponse.getStatusCode()), apiResponse.getResponse());
		}

		OAuthResponse oAuthResponse = jacksonMapper.convertJsonToObject(apiResponse.getResponse(), OAuthResponse.class);
		oAuthResponse.setStatusCode(apiResponse.getStatusCode());
		oAuthResponse.setResponse(apiResponse.getResponse());

		return oAuthResponse;
	}

	public APIResponse triggerEmail(String authToken, String requestUrl, String payload)
			throws MethodNotSupportedException, IOException {

		BaseRequest request = new BaseRequest(requestUrl + this.apiEndpointService.get(EndpointKeys.SFMCEndpointKey.TRIGGER_MAIL_API_PATH.name()),
				HttpMethodType.POST, payload);
		return restClient.sendRequest(request, getAuthHeaderMap(authToken));


	}

	private Map<String, String> getAuthHeaderMap(String authToken) {

		Map<String, String> headerMap = new HashMap<>();
		headerMap.put("Authorization", "Bearer " + authToken);

		return headerMap;
	}
	
	@Override
	public String getEndpointData(String key) {
		return this.apiEndpointService.get(key);
	}
}
