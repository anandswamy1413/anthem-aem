package com.anthem.platform.core.services.utility.impl;

import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.CFUtilityService;
import com.day.cq.commons.jcr.JcrConstants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

@Component(service = CFUtilityService.class, immediate = true)
@ServiceDescription("Utilities for content fragments Service")
public class CFUtilityServiceImpl implements CFUtilityService {

	@Reference
	private ResourceResolverFactory resourceResolverFactory;

	private final Logger log = LoggerFactory.getLogger(getClass());

	@Override
	public String getVarsAsJson(String cfPath) {
		cfPath = StringUtils.join(cfPath, PlatformConstants.FORWARD_SLASH, JcrConstants.JCR_CONTENT,
				PlatformConstants.FORWARD_SLASH, PlatformConstants.DATA);
		try (ResourceResolver resourceResolver = resourceResolverFactory
				.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)) {
			Resource cfResource = resourceResolver.getResource(cfPath);
			JsonObject finalJson = new JsonObject();
			if (cfResource.hasChildren()) {
				Iterator<Resource> variationIterator = cfResource.listChildren();
				while (variationIterator.hasNext()) {
					Resource variation = variationIterator.next();
					ValueMap valueMap = variation.getValueMap();
					Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
					String jsonStr = gson.toJson(valueMap);
					finalJson.addProperty(variation.getName(), jsonStr);
				}
				return finalJson.toString();
			}
		} catch (LoginException e) {
			log.error("Exception while copying asset {}", e);
		}
		return StringUtils.EMPTY;
	}
}
