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
import com.anthem.platform.core.services.endpoints.configs.ApigeeEndpointConfig;

@Designate(ocd = ApigeeEndpointConfig.class,factory=true)
@Component(immediate=true, service = ApiEndpointService.class, configurationPolicy=ConfigurationPolicy.OPTIONAL)
public class ApigeeEndpointServiceImpl implements ApiEndpointService{

	private Map<String,String> endpointConfigs;
	
	private String endpointId;
	
	@Activate
	@Modified
    public void activate(ApigeeEndpointConfig config) {
		endpointConfigs = new HashMap<>();
		this.endpointId = config.endpointId();
		endpointConfigs.put(EndpointKeys.ApigeeEndpointKey.APIGEE_API_KEY.name(), config.apigeeApiKey());
		endpointConfigs.put(EndpointKeys.ApigeeEndpointKey.APIGEE_HOST_URL.name(), config.apigeeHostUrl());
		endpointConfigs.put(EndpointKeys.ApigeeEndpointKey.API_CLIENT_ID.name(), config.apigeeClientID());
		endpointConfigs.put(EndpointKeys.ApigeeEndpointKey.API_CLIENT_SECRET.name(), config.apigeeClientSecretKey());
	}
	
	
	@Override
	public String get(String key) {
		return this.endpointConfigs.get(key);
	}
	
	@Override
	public Map<String, String> getEndpointConfigs() {
		return endpointConfigs;
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
