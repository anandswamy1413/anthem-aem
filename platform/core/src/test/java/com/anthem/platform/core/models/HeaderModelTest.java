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
class HeaderModelTest {

	private final AemContext context = new AemContext();

	private Resource resource;

	private HeaderModel headerModel;

	@BeforeEach
	void setUp() throws Exception {

		context.addModelsForClasses(HeaderModel.class);
		context.load().json("/com/anthem/platform/core/models/headermodel.json", "/content");
		resource = context.resourceResolver().getResource("/content/data");
		headerModel = resource.adaptTo(HeaderModel.class);
	}

	@Test
	void testAlttext() {
		final String expected = "desktop alt text";

		String actual = headerModel.getAltText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}

	@Test
	void testCheckBox() {
		final String expected = "false";

		String actual = headerModel.getCheckBox();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void tesfilereference() {
		final String expected = "/content/dam/test/headerlogo.PNG";

		String actual = headerModel.getFileReference();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testMobileCtaText() {
		final String expected = "Call now";

		String actual = headerModel.getMobileCtaText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testTimingText() {
		final String expected = "8 a.m. to 8p.m., 5 days a week";
		String actual = headerModel.getTimingText();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testLogoUrlPath() {
		final String expected = "/content/we-retail/us/en/men";
		String actual = headerModel.getLogoUrlPath();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testPhoneNumber() {
		String actual = headerModel.getPhoneNumber();
		assertNotNull(actual);
	}
	
	@Test
	void testCallingNumber() {
		String actual = headerModel.getCallingNumber();
		assertNotNull(actual);
	}
	
	@Test
	void testTelDetails() {
		final String expected = "(TTY :711)";
		String actual = headerModel.getTelDetails();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
	
	@Test
	void testTelIcon() {
		final String expected = "/content/dam/test/icon.PNG";
		String actual = headerModel.getTelIcon();
		assertNotNull(actual);
		assertEquals(expected, actual);
	}
}