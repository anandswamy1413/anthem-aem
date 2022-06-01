/**
 * 
 */
package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * @author nitishsaxena8
 *
 */

@ExtendWith(AemContextExtension.class)
class ColorBandTextModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private ColorBandTextModel colorBandTextModel;

	@BeforeEach
	void setUp() throws Exception {

		ctx.addModelsForClasses(BlockTextModel.class);
		ctx.load().json("/com/anthem/platform/core/models/ColorBandTextModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/color_band_text");
		colorBandTextModel = resource.adaptTo(ColorBandTextModel.class);

	}

	
	/**
	 * Test method for
	 * {@link com.anthem.platform.core.models.ColorBandTextModel#getOverlayText()}.
	 */
	@Test
	void testGetOverlayText() {
		final String expected = "test text";

		String actual = colorBandTextModel.getOverlayText();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.models.ColorBandTextModel#getTelIcon()}.
	 */
	@Test
	void testGetTelIcon() {
		final String expected = "/content/dam/anthem";

		String actual = colorBandTextModel.getTelIcon();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.models.ColorBandTextModel#getPhoneNumber()}.
	 */
	@Test
	void testGetPhoneNumber() {
		final String expected = "12345678901";

		String actual = colorBandTextModel.getPhoneNumber();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.models.ColorBandTextModel#getTelDetails()}.
	 */
	@Test
	void testGetTelDetails() {
		final String expected = "TTY-711";

		String actual = colorBandTextModel.getTelDetails();
		assertEquals(expected, actual);
	}

	
	/**
	 * Test method for
	 * {@link com.anthem.platform.core.models.ColorBandTextModel#getTimingText()}.
	 */
	@Test
	void testGetTimingText() {
		final String expected = "test timing";

		String actual = colorBandTextModel.getTimingText();
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetOverlayAriaText() {
		colorBandTextModel.getOverlayAriaText();
		assertNotNull(colorBandTextModel);
	}
	
	@Test
	void testGetCallingNumber() {
		colorBandTextModel.getCallingNumber();
		assertNotNull(colorBandTextModel);
	}

}
