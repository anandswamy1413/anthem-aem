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
class IconAndTextModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private IconAndTextModel iconAndTextModel;

	@BeforeEach
	void setUp() throws Exception {
		ctx.addModelsForClasses(IconAndTextModel.class);
		ctx.load().json("/com/anthem/platform/core/models/IconAndTextModel.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/icon_and_text");
		iconAndTextModel = resource.adaptTo(IconAndTextModel.class);
	}

	@Test
	void testGetProducts() {
		Resource actual = iconAndTextModel.getProducts();
		assertNotNull(actual);
	}
	
	@Test
	void testGetIconAriaText() {
		String expected = "icon text";
		String actual = iconAndTextModel.getIconAriaText();
		assertEquals(expected, actual);
	}
}
