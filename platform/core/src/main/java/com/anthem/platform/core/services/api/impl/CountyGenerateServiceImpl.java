package com.anthem.platform.core.services.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.beans.BaseRequest;
import com.anthem.platform.core.beans.CountyBean;
import com.anthem.platform.core.beans.HttpMethodType;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.api.ApiService;
import com.anthem.platform.core.services.api.GenerateAccessTokenService;
import com.anthem.platform.core.services.api.configs.CountyGenerateServiceConfig;
import com.anthem.platform.core.services.endpoints.ApiEndpointFactory;
import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.services.utility.RestClient;
import com.anthem.platform.core.utils.PlatformUtils;

import acscommons.com.google.common.net.HttpHeaders;

@Designate(ocd = CountyGenerateServiceConfig.class)
@Component(service = ApiService.class, immediate = true, property = { "apiId=" + CountyGenerateServiceImpl.API_ID },configurationPolicy=ConfigurationPolicy.OPTIONAL)
public class CountyGenerateServiceImpl implements ApiService<String, List<CountyBean>> {

	public static final String API_ID = "County Population";
	public static final String API_DESC = "Service to get county data based on zipcode";
	public static final String ERROR_CODE_UPDATE_DATA = "API:REST:COUNTY_GET_FAILED";
	private static final Logger LOG = LoggerFactory.getLogger(CountyGenerateServiceImpl.class);
	
	private String countyGenerationEndpoint;

	@Reference
	private GenerateAccessTokenService generateAccessTokenService;

	@Reference
    private RestClient restClient;
	
	@Reference
	private JacksonMapperService jacksonMapper;
	
	@Reference
	private ApiEndpointFactory apiEndpointFactory;
	
	private ApiEndpointService apiEndpointService;

	@Override
	public String getApiId() {
		return API_ID;
	}

	@Override
	public String getDesc() {
		return API_DESC;
	}
	
	@Activate
	@Modified
    public void activate(CountyGenerateServiceConfig config) {
		String endpointId = config.endpointId();
		this.apiEndpointService = apiEndpointFactory.getEndpoint(endpointId);
		this.countyGenerationEndpoint = this.apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.APIGEE_HOST_URL.name()) + config.countyPopulationEndpoint();		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CountyBean> getResults(String apiData, Map<String, Object> metadata) throws ApiException{
		String accessToken = generateAccessTokenService.getToken();
		List<CountyBean> countyList = new ArrayList<>();
		try {
			List<BasicNameValuePair> bodyParams = new LinkedList<>();
			bodyParams.add(new BasicNameValuePair("zipcode", apiData));
			String endPoint = this.countyGenerationEndpoint+"?"+PlatformUtils.getUrlParams(bodyParams);
			BaseRequest request = new BaseRequest(endPoint, HttpMethodType.GET, StringUtils.EMPTY);
			Map<String, String> headerMap = new HashMap<>();
            headerMap.put(HttpHeaders.AUTHORIZATION, PlatformConstants.BEARER_SPACE+accessToken);
            headerMap.put(PlatformConstants.APIKEY, this.apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.APIGEE_API_KEY.name()));
            APIResponse response = restClient.sendRequest(request,headerMap);
            int status = response.getStatusCode();
			if (status == 200) {				
				countyList = jacksonMapper.convertJsonToObject(response.getResponse(), List.class);
			} 
		} catch (MethodNotSupportedException | IOException e) {
			throw new ApiException(ERROR_CODE_UPDATE_DATA, "Error during getting county",e);
		}
		return countyList;
	}

	@Override
	public List<CountyBean> updateData(String apiData, Map<String, Object> metadata) throws ApiException {
		return new ArrayList<>();
	}
	
	@Override
	public String getEndpointData(String key) {
		return this.apiEndpointService.get(key);
	}

}
