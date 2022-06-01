package com.anthem.platform.core.services.api;

import java.io.IOException;

import org.apache.http.MethodNotSupportedException;

import com.anthem.platform.core.beans.APIResponse;
import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.beans.OAuthRequest;
import com.anthem.platform.core.beans.OAuthResponse;

public interface SFMCConnectorService {

	public OAuthResponse getAuthTokenResponse(OAuthRequest oAuthRequest, String authUrl)
			throws ApiException, MethodNotSupportedException, IOException ;
	
	public APIResponse triggerEmail(String accessToken, String restInstanceUrl, String payload) throws MethodNotSupportedException, IOException;

	public String getEndpointData(String key);
}
