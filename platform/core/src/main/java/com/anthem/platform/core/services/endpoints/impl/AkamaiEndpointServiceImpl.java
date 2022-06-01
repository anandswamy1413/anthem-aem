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
import com.anthem.platform.core.services.endpoints.configs.AkamaiEndPointServiceConfig;

@Designate(ocd = AkamaiEndPointServiceConfig.class,factory=true)
@Component(immediate=true, service = ApiEndpointService.class, configurationPolicy=ConfigurationPolicy.OPTIONAL)
public class AkamaiEndpointServiceImpl implements ApiEndpointService{

	private Map<String,String> endpointConfigs;
	
	private String endpointId;
	
	@Activate
	@Modified
    public void activate(AkamaiEndPointServiceConfig config) {
		endpointConfigs = new HashMap<>();
		this.endpointId = config.endpointId();
		endpointConfigs.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_ACCESS_TOKEN.name(), config.akamaiAccessToken());
		endpointConfigs.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_HOST_URL.name(), config.akamaiHostUrl());
		endpointConfigs.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_CLIENT_TOKEN.name(), config.akamaiClientToken());
		endpointConfigs.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_CLIENT_SECRET.name(), config.akamaiClientSecretKey());
		endpointConfigs.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_CONNECTION_TIMEOUT.name(), config.connectionTimeOut());
		endpointConfigs.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_SOCKET_TIMEOUT.name(), config.socketTimeOut());
		endpointConfigs.put(EndpointKeys.AkamaiEndpointKey.AKAMAI_REQUEST_TIMEOUT.name(), config.requestTimeOut());
	}
	
	@Override
	public String getEndpointId() {
		return endpointId;
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
	
}
