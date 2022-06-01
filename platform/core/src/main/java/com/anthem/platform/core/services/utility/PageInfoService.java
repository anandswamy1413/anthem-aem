package com.anthem.platform.core.services.utility;

import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.day.cq.wcm.api.Page;

import org.apache.sling.api.resource.ResourceResolver;

public interface PageInfoService {
	
	public Locale getPageLocale(Page page, ResourceResolver resourceResolver);
	
	public String getPageStateCode(Page page);
	
	public String getPageTitle(Page page);
	
	public String getAnalyticsTitle(Page page);
	
	public String getAnalyticsPageName(Page page, int pageNameLevel);
	
	public Set<String> getRunModes();
	
	public String getRunMode();
	
	public String getPageStateName(Page page);
	
	

	public String getSeoTitle(Page page);
	/*
	 * ValueMap getInheritedContentValueMap(Page page);
	 * 
	 * String getBrand(Page page);
	 */

	public String getAnalyticsName(Page page);

	public String getContextData(Map<String, String> dataSource, String key, String brandName);
}
