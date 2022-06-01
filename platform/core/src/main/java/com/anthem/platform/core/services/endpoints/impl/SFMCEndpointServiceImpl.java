package com.anthem.platform.core.services.endpoints.impl;

import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.metatype.annotations.Designate;

import com.anthem.platform.core.services.endpoints.ApiEndpointService;
import com.anthem.platform.core.services.endpoints.EndpointKeys;
import com.anthem.platform.core.services.endpoints.configs.SFMCEndpointConfig;

@Designate(ocd = SFMCEndpointConfig.class,factory=true)
@Component(service = ApiEndpointService.class,configurationPolicy=ConfigurationPolicy.OPTIONAL,immediate = true)
public class SFMCEndpointServiceImpl implements ApiEndpointService {

	private Map<String,String> endpointConfigs;
	
	private String endpointId;
	
	@Activate
	@Modified
    public void activate(SFMCEndpointConfig config) {
		endpointConfigs = new HashMap<>();
		this.endpointId = config.endpointId();
		endpointConfigs.put(EndpointKeys.SFMCEndpointKey.ACCOUNT_ID.name(), config.accountId());
		endpointConfigs.put(EndpointKeys.SFMCEndpointKey.AUTH_URL.name(),config.authUrl());
		endpointConfigs.put(EndpointKeys.SFMCEndpointKey.CLIENT_ID.name(), config.clientId());
		endpointConfigs.put(EndpointKeys.SFMCEndpointKey.CLIENT_SECRET.name(), config.clientSecret());
		endpointConfigs.put(EndpointKeys.SFMCEndpointKey.GRANT_TYPE.name(), config.grantType());
		endpointConfigs.put(EndpointKeys.SFMCEndpointKey.TRIGGER_MAIL_API_PATH.name(), config.triggerMailAPIPath());
		endpointConfigs.put(EndpointKeys.SFMCEndpointKey.SUBSCRIBE_API_PATH.name(), config.subscribeAPIPath());
	}

	@Override
	public Map<String, String> getEndpointConfigs() {
		return endpointConfigs;
	}
	
	@Override
	public String get(String key) {
		return this.endpointConfigs.get(key);
	}

	@Override
	public void add(String key,String value) {
		if(null == this.endpointConfigs) {
			this.endpointConfigs = new HashMap<>();
		}
		this.endpointConfigs.put(key, value);		
	}

	@Override
	public String getEndpointId() {
		return this.endpointId;
	}
	

}
