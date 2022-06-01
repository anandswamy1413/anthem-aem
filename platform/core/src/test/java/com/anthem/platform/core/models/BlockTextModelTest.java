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
class BlockTextModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private BlockTextModel blockText;

	@BeforeEach
	void setUp() throws Exception {
		ctx.addModelsForClasses(BlockTextModel.class);
		ctx.load().json("/com/anthem/platform/core/models/BlockTextModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/block_text");
		blockText = resource.adaptTo(BlockTextModel.class);
	}

	@Test
	void testGetBorderSwitch() {
		final String expected = "false";
		String actual = blockText.getBorderSwitch();
		assertNotNull(blockText);
		assertEquals(expected, actual);
	}

	@Test
	void TestGetBodyText() {
		final String expected = "test body";
		String actual = blockText.getTextSection();
		assertEquals(expected, actual);
	}

	@Test
	void testGetBodyAriaText() {
		final String expected = "test body";

		String actual = blockText.getBodyAriaText();
		assertEquals(expected, actual);
	}

}
