package com.anthem.platform.core.services.utility.impl;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adobe.acs.commons.genericlists.GenericList;
import com.adobe.acs.commons.genericlists.GenericList.Item;
import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.configs.GenericListConfig;
import com.anthem.platform.core.utils.PlatformHelper;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

import junit.framework.Assert;

class GenericListServiceImplTest {

	@InjectMocks
	private GenericListServiceImpl genericlistServiceImpl;
	
	@Mock
	private ResourceResolverFactory resourceResolverFactory;
	
	@Mock
	private ResourceResolver resourceResolver;
	
	@Mock
	private GenericListConfig config;
	
	@Mock
	private PageManager pageManager;
	
	@Mock
	private Page listPage;
	
	@Mock
	private GenericList generic;

	@InjectMocks
	private List<Item> itemsList=new ArrayList<>();
	
	@Mock
	private Map<String,String> genericListMap;
	
	@Mock
    private Item item;
	
	@BeforeEach
	void setUp() throws Exception
	{ MockitoAnnotations.initMocks(this);
	   itemsList.add(item); 
	   itemsList.add(item);
	 when(config.genericListRoot()).thenReturn("/etc/acs-commons/lists/anthem/");
	 genericlistServiceImpl.activate(config);
	 when(resourceResolverFactory.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)).thenReturn(resourceResolver);
	 when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
	 when(pageManager.getPage("/etc/acs-commons/lists/anthem/Asset_list")).thenReturn(listPage);
	 when(listPage.adaptTo(GenericList.class)).thenReturn(generic);
	 when(generic.getItems()).thenReturn(itemsList);
	}	

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.GenericListServiceImpl#getGenericListAsMap()}.
	 */
	@Test
	void testGetGenericListAsMap() 
	{ 
		Assert.assertNotNull(genericlistServiceImpl.getGenericListAsMap("Asset_list"));
	
	}

}
