package com.anthem.platform.core.services.utility;

import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.constants.PlatformConstants;
import com.anthem.platform.core.services.utility.impl.XFUtilityServiceImpl;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageFilter;

class XFUtilityServiceTest {

	@InjectMocks
	XFUtilityServiceImpl xFUtilityServiceImpl;
	
	@Mock
	private ResourceResolverFactory resourceResolverFactory;
	
	@Mock
	ResourceResolver resourceResolver;
	
	@Mock
	Resource xfResource;
	

	@Mock
	Resource resource;
	
	@Mock
	Page page;
	
	@Mock
	Page fragmentPage;
	
	private static final String XF_RESOURCE_TYPE = "cq/experience-fragments/components/experiencefragment";
	
	@Mock
	Iterator<Page> fragmentVariation;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testGetApiKey() throws LoginException {
		when(resourceResolverFactory.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO)).thenReturn(resourceResolver);	
		when(resourceResolver.getResource("content/sites/xfPath")).thenReturn(xfResource);
		when(xfResource.adaptTo(Page.class)).thenReturn(page);
		when(page.getContentResource()).thenReturn(resource);
		when(resource.isResourceType(XF_RESOURCE_TYPE)).thenReturn(true,false);
		when(page.listChildren(Mockito.any(PageFilter.class),Mockito.any(Boolean.class))).thenReturn(fragmentVariation);
		when(fragmentVariation.hasNext()).thenReturn(true,false);
		when(fragmentVariation.next()).thenReturn(fragmentPage);
		xFUtilityServiceImpl.getAllXFVariationsPaths("content/sites/xfPath");
	}

}
