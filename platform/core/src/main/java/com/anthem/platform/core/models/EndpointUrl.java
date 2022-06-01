package com.anthem.platform.core.models;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import com.adobe.acs.commons.models.injectors.annotation.HierarchicalPageProperty;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;

import com.anthem.platform.core.services.utility.GenericListService;
import com.anthem.platform.core.services.utility.PageInfoService;

@Model(adaptables = { SlingHttpServletRequest.class, Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EndpointUrl {
	
	private static final String LIST_NAME = "endpoints";
	private static final String CP_CODE = "akamai_cp_codes";
	
	private Map<String,String> endpointMap;
	private Map<String,String> cpCodesMap;
	
	@Inject
	private GenericListService genericListService;
	
	@Inject
	private PageInfoService pageInfoService;
	
	@RequestAttribute
	private String endpointKey;

	@HierarchicalPageProperty
	private String brandName;
	
	@PostConstruct
	protected void init() {
		endpointMap = genericListService.getGenericListAsMap(LIST_NAME);
		cpCodesMap = genericListService.getGenericListAsMap(CP_CODE);
	}

	public Map<String, String> getEndpointMap() {
		return endpointMap;
	}

	public Map<String, String> getCpCodesMap() {
		return cpCodesMap;
	}
	
	public String getContextEndpoint() {
		return pageInfoService.getContextData(endpointMap, endpointKey, brandName);
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
}
