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
class CountdownPromoModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private CountdownPromoModel countdownPromoModel;

	@BeforeEach
	void setUp() throws Exception {

		ctx.addModelsForClasses(CountdownPromoModel.class);
		ctx.load().json("/com/anthem/platform/core/models/CountdownPromoModel.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/countdown_promo");
		countdownPromoModel = resource.adaptTo(CountdownPromoModel.class);

	}

	
	/**
	 * Test method for
	 * {@link com.anthem.platform.core.models.CountdownPromoModel#getEndDate()}.
	 */
	@Test
	void testGetEndDate() {
		final String expected = "YYYY-MM-DD";

		String actual = countdownPromoModel.getEndDate();
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.models.CountdownPromoModel#getPromoText()}.
	 */
	@Test
	void testGetTelIcon() {
		final String expected = "Promotional Text";

		String actual = countdownPromoModel.getPromoText();
		assertEquals(expected, actual);
	}

	@Test
	void testGetPromoAriaText() {
		countdownPromoModel.getPromoAriaText();
		assertNotNull(countdownPromoModel);
	}

}
