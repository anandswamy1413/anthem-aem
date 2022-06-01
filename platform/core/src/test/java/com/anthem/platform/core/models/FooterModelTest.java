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
class FooterModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private FooterModel footerModel;

	@BeforeEach
	void setUp() throws Exception {

		ctx.addModelsForClasses(FooterModel.class);
		ctx.load().json("/com/anthem/platform/core/models/FooterModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/footer_model");
		footerModel = resource.adaptTo(FooterModel.class);

	}

	@Test
	void testGetTitle() {
		final String expected = "test title";
		String actual = footerModel.getTitle();
		assertNotNull(footerModel);
		assertEquals(expected, actual);
	}

	@Test
	void TestGetZipcode() {
		final String expected = "654328";
		String actual = footerModel.getZipcode();
		assertEquals(expected, actual);
	}
}
