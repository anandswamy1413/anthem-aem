package com.anthem.platform.core.services.endpoints;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("squid:S2221")
@Component(service = ApiEndpointFactory.class, immediate = true, enabled = true)
public class ApiEndpointFactory {

	protected final Logger log = LoggerFactory.getLogger(ApiEndpointFactory.class);

	private Map<String, ApiEndpointService> endpointMap = new HashMap<>();

	@Reference(cardinality = ReferenceCardinality.MULTIPLE, policy = ReferencePolicy.DYNAMIC)
	protected synchronized void bind(ApiEndpointService apiEndpoint) {

		try {
			String endpointId = apiEndpoint.getEndpointId();
			if (StringUtils.isNotBlank(endpointId)) {
				log.info("Added to Api endpoint map:{}", endpointId);
				endpointMap.put(endpointId, apiEndpoint);
			}
		} catch (Exception e) {
			log.error("Please contact administrator as something went wrong in api selector bind()", e);
		}
	}

	protected synchronized void unbind(ApiEndpointService apiEndpoint) {
		try {
			String endpointId = apiEndpoint.getEndpointId();
			if (StringUtils.isNotBlank(endpointId)) {
				endpointMap.remove(endpointId, apiEndpoint);
				log.info("Removed from Api endpoint map:{}", endpointId);
			}

		} catch (Exception e) {
			log.error("Please contact administrator as something went wrong in api selector unbind()", e);
		}
	}

	public Map<String, ApiEndpointService> getEndpointMap() {
		return endpointMap;
	}

	public ApiEndpointService getEndpoint(String endpointId) {
		log.info("Searching for endpoint of id:{} found value:{}", endpointId, endpointMap.get(endpointId));
		return endpointMap.getOrDefault(endpointId, null);
	}

}
