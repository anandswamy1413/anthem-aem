package com.anthem.platform.core.services.api.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicNameValuePair;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.BaseRequest;
import com.anthem.platform.core.beans.HttpMethodType;
import com.anthem.platform.core.beans.OAuthResponse;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.api.GenerateAccessTokenService;
import com.anthem.platform.core.services.api.configs.GenerateAccessTokenServiceConfig;
import com.anthem.platform.core.services.endpoints.ApiEndpointFactory;
import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.anthem.platform.core.services.utility.RestClient;
import com.anthem.platform.core.utils.PlatformUtils;

/**
 * Service for getting access token.
 *
 * @author aswaroop
 */
@Component(service = GenerateAccessTokenService.class, immediate = true)
@ServiceDescription("Get access token")
@Designate(ocd = GenerateAccessTokenServiceConfig.class)
public class GenerateAccessTokenServiceImpl implements GenerateAccessTokenService {
    
    static String basicSpace = "Basic ";
    private final Logger log = LoggerFactory.getLogger(GenerateAccessTokenServiceImpl.class);
    @Reference
    RestClient restClient;
    String token; //this is class level variable to store token value.
	
	private String accessTokenEndpoint;
	
	@Reference
	private JacksonMapperService jacksonMapper;

	@Reference
	private ApiEndpointFactory apiEndpointFactory;
	
	private ApiEndpointService apiEndpointService;
    
    @Activate
	@Modified
    public void activate(GenerateAccessTokenServiceConfig config) {
    	String endpointId = config.endpointId();
		this.apiEndpointService = apiEndpointFactory.getEndpoint(endpointId);
		this.accessTokenEndpoint = this.apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.APIGEE_HOST_URL.name()) + config.apigeeAccessTokenEndpoint();		
	}
    
    @Override
    public String generateNewToken() {
    	List<BasicNameValuePair> bodyParams = new LinkedList<>();
    	bodyParams.add(new BasicNameValuePair(PlatformConstants.SCOPE, PlatformConstants.PUBLIC));
    	bodyParams.add(new BasicNameValuePair(PlatformConstants.GRANT_TYPE, PlatformConstants.CLIENT_CREDS));
    	String params = PlatformUtils.getUrlParams(bodyParams);
        BaseRequest authRequest = new BaseRequest(accessTokenEndpoint, HttpMethodType.POST, params);
        Map<String, String> authHeader = new HashMap<>();
        authHeader.put(PlatformConstants.APIKEY, this.apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.APIGEE_API_KEY.name()));
        authHeader.put(HttpHeaders.AUTHORIZATION, basicSpace + getEncodedKey());
        authHeader.put(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_FORM_URLENCODED.getMimeType());       
        try {
            APIResponse response = restClient.sendRequest(authRequest, authHeader);

            if (response.getStatusCode() == 200) {                
                OAuthResponse oAuthResponse = jacksonMapper.convertJsonToObject(response.getResponse(), OAuthResponse.class);
                token = oAuthResponse.getAccessToken();
                log.info("Access Token generated successfully, issued at {} and Expires in {}", oAuthResponse.getIssuedAt(),oAuthResponse.getExpiresIn());
            }
        } catch (IOException e) {
            log.error("Exception in executing command{}", e.getLocalizedMessage());
        } catch (MethodNotSupportedException e) {
            log.error("Exception while calling command method {}", e.getLocalizedMessage());
        } 
        return token;
    }
    //TO DO: Base64
    private String getEncodedKey() {
        String encodedAuth = this.apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.API_CLIENT_ID.name()) + PlatformConstants.COLON + this.apiEndpointService.get(EndpointKeys.ApigeeEndpointKey.API_CLIENT_SECRET.name());
        try {
			return Base64.getEncoder().encodeToString(encodedAuth.getBytes(StandardCharsets.UTF_8.name()));
		} catch (UnsupportedEncodingException e) {
            log.error("Exception while getting key in method getEncodedKey {}", e.getLocalizedMessage());
		} return StringUtils.EMPTY;
    }

    public String getToken() {
    	if (StringUtils.isBlank(token)) {
    		token = generateNewToken();
        }
        return token;
    }
}