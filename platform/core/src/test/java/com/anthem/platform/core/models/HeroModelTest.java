package com.anthem.platform.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(AemContextExtension.class)
class HeroModelTest {

	private final AemContext context = new AemContext();

	private Resource resource;

	private HeroModel heroModel;

	@BeforeEach
	void setUp() throws Exception {

		context.addModelsForClasses(HeroModel.class);
		context.load().json("/com/anthem/platform/core/models/heromodel.json", "/content");
		resource = context.resourceResolver().getResource("/content/data");
		heroModel = resource.adaptTo(HeroModel.class);
	}

	@Test
	void testAltText() {
		final String expected = "hello";

		String actual = heroModel.getAltText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	
	@Test
	void testAlignText() {
		final String expected = "text-left";

		String actual = heroModel.getAlignText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}	
	
	@Test
	void testBodyText() {
		final String expected = "hello body";

		String actual = heroModel.getBodyText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
     
	
	@Test
	void testPhoneNumber() {
		final String expected = "6476476";

		String actual = heroModel.getPhoneNumberHero();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testTtyDetails() {
		final String expected = "tty";

		String actual = heroModel.getTelHeroDetails();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testFileReference() {
		final String expected = "/content/dam/test/headerlogo.PNG";
		String actual = heroModel.getFileReference();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testBodyAriaText() {
		String actual = heroModel.getBodyAriaText();
		assertNotNull(actual);
	}
	
	@Test
	void testMobileFileReference() {
		final String expected = "/content/dam/test/mobile/headerlogo.PNG";
		String actual = heroModel.getMobileFileReference();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testTabletFileReference() {
		final String expected = "/content/dam/test/tablet/headerlogo.PNG";
		String actual = heroModel.getTabletFileReference();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testTextColor() {
		final String expected = "#3f3f3f";
		String actual = heroModel.getTextColor();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testTabletAlignText() {
		final String expected = "text-right";
		String actual = heroModel.getTabletAlignText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testMobileAlignText() {
		final String expected = "text-center";
		String actual = heroModel.getMobileAlignText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testSubText() {
		final String expected = "sample subtext";
		String actual = heroModel.getSubText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
}