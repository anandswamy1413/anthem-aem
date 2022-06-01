package com.anthem.platform.core.models;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class PlanZipcodeWidgetPairModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private PlanZipcodeWidgetPairModel planWidget;
	
	@BeforeEach
	void setUp() throws Exception {
		ctx.addModelsForClasses(PlanZipcodeWidgetPairModel.class);
		ctx.load().json("/com/anthem/platform/core/models/PlanZipcodeWidgetPairModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/plan_Widget");
		planWidget = resource.adaptTo(PlanZipcodeWidgetPairModel.class);
	}
	
	@Test
	void testGetLobType() {
		planWidget.getJson();
	}

}
