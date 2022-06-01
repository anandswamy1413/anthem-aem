package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class PlanZipcodeWidgetModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private PlanZipcodeWidgetModel planZipcodeWidgetModel;

	@BeforeEach
	void setUp() throws Exception {
		ctx.addModelsForClasses(PlanZipcodeWidgetModel.class);
		ctx.load().json("/com/anthem/platform/core/models/PlanZipcodeWidgetModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/zipcode-widget");
		planZipcodeWidgetModel = resource.adaptTo(PlanZipcodeWidgetModel.class);

	}

	@Test
	void testGetLobType() {
		final String expected = "Lob Type";
		String actual = planZipcodeWidgetModel.getLobType();
		assertEquals(expected, actual);
	}

	@Test
	void testGetBrandCFUrl() {
		final String expected = "anthem/platform/dam/assets/demo";
		String actual = planZipcodeWidgetModel.getBrandCFUrl();
		assertEquals(expected, actual);
	}

	@Test
	void testSetBrandCFUrl() {
		planZipcodeWidgetModel.setBrandCFUrl("anthem/platform/dam/assets/demo");
		assertNotNull(planZipcodeWidgetModel);
	}
	
	@Test
	void testSetLobType() {
		planZipcodeWidgetModel.setLobType("Lob Type");
		assertNotNull(planZipcodeWidgetModel);
	}

}
