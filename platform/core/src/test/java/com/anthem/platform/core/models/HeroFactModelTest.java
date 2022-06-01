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
class HeroFactModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private HeroFactModel heroFactModel;

	@BeforeEach
	void setUp() throws Exception {

		ctx.addModelsForClasses(HeroFactModel.class);
		ctx.load().json("/com/anthem/platform/core/models/HeroFactModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/hero_fact");
		heroFactModel = resource.adaptTo(HeroFactModel.class);

	}

	@Test
	void testGetHeading() {
		final String expected = "Sample Heading";
		String actual = heroFactModel.getHeading();
		assertNotNull(heroFactModel);
		assertEquals(expected, actual);
	}

	@Test
	void testGetHeadingAriaLabel() {
		final String expected = "Sample Heading";
		String actual = heroFactModel.getHeadingAriaLabel();
		assertNotNull(heroFactModel);
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetSubHeading() {
		final String expected = "Sample subheading";
		String actual = heroFactModel.getSubHeading();
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetTextAlign() {
		final String expected = "center";
		String actual = heroFactModel.getTextAlign();
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetSubHeadingAriaLabel() {
		final String expected = "Sample subheading";
		String actual = heroFactModel.getSubHeadingAriaLabel();
		assertEquals(expected, actual);
	}
}
