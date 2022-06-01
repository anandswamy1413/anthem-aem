package com.anthem.platform.core.models.datalayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.io.IOException;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;

import com.anthem.platform.core.services.utility.JacksonMapperService;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.commons.lang3.StringUtils;

class AbstractDigitalDataTest {

	Page currentPage;

	@Mock
	PageManager pageManager;

	@Mock
	Resource resource;

	@Mock
	JacksonMapperService jacksonMapper;

	@InjectMocks
	AbstractDigitalData abstractDigitalData;

	@Mock
	ResourceResolver resourceResolver;

	@Mock
	private Logger logger;

	String EMPTY_JSON_OBJECT = "{}";

	@BeforeEach
	public void setup() throws IOException {
		initMocks(this);
		assertNotNull(pageManager);
		assertNotNull(abstractDigitalData);
	}

	@Test
	void testInitBase() {
		when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		assertEquals(pageManager, resourceResolver.adaptTo(PageManager.class));
		when(pageManager.getContainingPage(resource)).thenReturn(currentPage);
		assertEquals(currentPage, pageManager.getContainingPage(resource));
		abstractDigitalData.initBase();
	}

	@Test
	void testGetJsonIf() {
		when(jacksonMapper.convertObjectToJson(this, true)).thenReturn("jackson");
		assertEquals("jackson", jacksonMapper.convertObjectToJson(this, true));
		abstractDigitalData.getJson();
	}

}
