package com.anthem.platform.core.services.utility.impl;

import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ContentFragmentJsonServiceImplTest {

	@Mock
	Resource resource;

	@Mock
	ResourceResolver resolver;

	@Mock
	Resource resourceContent;

	@Mock
	ValueMap valueMap;

	@Mock
	Set<Entry<String, Object>> entrySet;

	@Mock
	Iterator<Map.Entry<String, Object>> itr;
	
	@InjectMocks
	ContentFragmentJsonServiceImpl conFragment;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		when(resource.getPath()).thenReturn("cfPath");
		when(resolver.getResource("cfPath" + conFragment.CF_PATH + "contentFragmentName")).thenReturn(resourceContent);
		when(resourceContent.getValueMap()).thenReturn(valueMap);
		when(valueMap.entrySet()).thenReturn(entrySet);
		when(entrySet.iterator()).thenReturn(itr);
	}

	@Test
	void testCFToJson() {
		conFragment.contentFragmentToJson(resource, "contentFragmentName", resolver);
	}

}
