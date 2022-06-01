package com.anthem.platform.core.services.utility.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.Designate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.GenericListService;
import com.anthem.platform.core.services.utility.configs.GenericListConfig;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

@Designate(ocd = GenericListConfig.class)
@Component(immediate=true, service = GenericListService.class)
public class GenericListServiceImpl implements GenericListService{
	
	private static final Logger LOG = LoggerFactory.getLogger(GenericListServiceImpl.class);

	private String listRoot;
	
	@Reference
	private ResourceResolverFactory resourceResolverFactory;
	
	@Activate
	public void activate(GenericListConfig config) {
		this.listRoot = config.genericListRoot();
	}
	
	@Override
	public Map<String, String> getGenericListAsMap(String listName) {
		Map<String,String> genericListMap = new LinkedHashMap<>();
		try (ResourceResolver resourceResolver = resourceResolverFactory.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)) {
			String genericlistPagePath = this.listRoot + listName;
			PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
			Page listPage = pageManager.getPage(genericlistPagePath);
			if(null != listPage) {
				GenericList list = listPage.adaptTo(GenericList.class);
				List<Item> itemsList = list.getItems();
				for(Item item : itemsList) {
					genericListMap.put(item.getValue(), item.getTitle());
				}
			}			
		} catch (LoginException e) {
			LOG.error("Exception getting service resource resolver ",e);
		}
		return genericListMap;
	} 

}
