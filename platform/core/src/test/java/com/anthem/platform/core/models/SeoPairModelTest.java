package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import io.wcm.testing.mock.aem.junit5.AemContext;

class SeoPairModelTest {

	private final AemContext ctx = new AemContext();

	@Mock
	private Resource resource;

	@InjectMocks
	private SeoPairModel seoPairModel;

	private List<SeoPair> seoPairList = new ArrayList<>();

	@Mock
	private SeoPair seoPair;

	private List<Resource> seoPairs = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		ctx.addModelsForClasses(SeoPair.class);
		ctx.load().json("/com/anthem/platform/core/models/SeoPairModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/seo-pair");
		seoPairs.add(resource);
		seoPair = resource.adaptTo(SeoPair.class);
		seoPairList.add(seoPair);
	}

	@Test
	void test() {
		seoPairModel.init();
		assertNotNull(seoPairModel);
	}

	@Test
	void testGetSeoPairList() {
		seoPairModel.getSeoPairList();
		assertNotNull(seoPairModel);
	}
	
	@Test
	void testGetDisableFontPreload() {
		final String actual = seoPairModel.getDisableFontPreload();
	}

}
