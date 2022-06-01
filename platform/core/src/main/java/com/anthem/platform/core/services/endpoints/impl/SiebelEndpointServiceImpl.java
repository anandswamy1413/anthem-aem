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
import com.anthem.platform.core.services.endpoints.configs.SiebelEndpointConfig;

@Designate(ocd = SiebelEndpointConfig.class,factory=true)
@Component(service = ApiEndpointService.class,configurationPolicy=ConfigurationPolicy.OPTIONAL,immediate = true)
public class SiebelEndpointServiceImpl implements ApiEndpointService{
	
	private Map<String,String> endpointConfigs;
	
	private String endpointId;
	
	@Activate
	@Modified
    public void activate(SiebelEndpointConfig config) {
		endpointConfigs = new HashMap<>();
		this.endpointId = config.endpointId();
		endpointConfigs.put(EndpointKeys.SiebelEndpointKey.SIEBEL_HOST_URL.name(), config.siebelHostUrl());
		endpointConfigs.put(EndpointKeys.SiebelEndpointKey.SOAP_DEBUG_ENABLED.name(), String.valueOf(config.enableDebugLogsForSoap()));
		endpointConfigs.put(EndpointKeys.SiebelEndpointKey.ROUTING_CODE.name(),config.routingCode());
		endpointConfigs.put(EndpointKeys.SiebelEndpointKey.VENDOR_SOURCE.name(),config.vendorSource());
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
	public String getEndpointId() {
		return this.endpointId;
	}
	
	@Override
	public void add(String key,String value) {
		if(null == this.endpointConfigs) {
			this.endpointConfigs = new HashMap<>();
		}
		this.endpointConfigs.put(key, value);		
	}

	
}
