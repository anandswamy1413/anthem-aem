package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;


@ExtendWith(AemContextExtension.class)
class SeoPairTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private SeoPair seoPair;

	@BeforeEach
	void setUp() throws Exception {
		ctx.addModelsForClasses(SeoPair.class);
		ctx.load().json("/com/anthem/platform/core/models/SeoPairModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/seo-pair");
		seoPair = resource.adaptTo(SeoPair.class);
		assertNotNull(seoPair);
	}

	@Test
	void testGetTagType() {
		final String expected = "Key";
		String actual = seoPair.getTagType();
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetTagName() {
		final String expected = "Value";
		String actual = seoPair.getTagName();
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetTagValue() {
		final String expected = "Tag Value";
		String actual = seoPair.getTagValue();
		assertEquals(expected, actual);
	}
	
}
