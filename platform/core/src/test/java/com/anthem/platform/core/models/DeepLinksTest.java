package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class DeepLinksTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private DeepLinks deepLinks;

	@BeforeEach
	void setUp() throws Exception {
		ctx.addModelsForClasses(DeepLinks.class);
		ctx.load().json("/com/anthem/platform/core/models/DeepLinksTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/deep_links");
		deepLinks = resource.adaptTo(DeepLinks.class);
	}

	@Test
	void testGetParamName() {
		String expected = "param name";
		String actual = deepLinks.getParamName();
		assertNotNull(deepLinks);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetParamValue() {
		String expected = "param value";
		String actual = deepLinks.getParamValue();
		assertEquals(expected, actual);
	}
}
