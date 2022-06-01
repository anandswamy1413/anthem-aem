package com.anthem.platform.core.services.utility.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.constants.PlatformConstants;
import com.google.gson.Gson;

class CFUtilityServiceImplTest {

	@InjectMocks
	private CFUtilityServiceImpl cfUtility;

	@Mock
	private ResourceResolverFactory resourceResolverFactory;

	@Mock
	private ResourceResolver resourceResolver;
	
	@Mock
	private Resource cfResource;
	
	@Mock
	private Iterator<Resource> variationIterator;
	
	@Mock
	private Resource variation;

	private final String cfPath = "/content/dam/tfn-generic/default";
	
	@Mock
	private ValueMap valueMap;

	@Mock
	private Gson gson;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(resourceResolverFactory.getServiceResourceResolver(PlatformConstants.ANTHEM_AUTO_INFO))
				.thenReturn(resourceResolver);
		when(resourceResolver.getResource("/content/dam/tfn-generic/default/jcr:content/data")).thenReturn(cfResource);
    when(cfResource.hasChildren()).thenReturn(true);
    when(cfResource.listChildren()).thenReturn(variationIterator);
    when(variationIterator.hasNext()).thenReturn(true, false);
    when(variationIterator.next()).thenReturn(variation);
    when(variation.getValueMap()).thenReturn(valueMap);
    when(gson.toJson(valueMap)).thenReturn("seo:123-456-789");
    when(variation.getName()).thenReturn("master");
	}

	
	  @Test void testGetVarsAsJson() {
	  assertNotNull(cfUtility);
	  cfUtility.getVarsAsJson(cfPath);
	  }
	 

}

