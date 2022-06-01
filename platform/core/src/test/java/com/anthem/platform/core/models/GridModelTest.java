package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class GridModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	private GridModel gridModel;

	@Mock
	private int[] numberOfGrids;

	List<Integer> columnsList = new ArrayList<>();

	@BeforeEach
	void setUp() throws Exception {
		ctx.addModelsForClasses(GridModel.class);
		ctx.load().json("/com/anthem/platform/core/models/GridModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/grid");
		gridModel = resource.adaptTo(GridModel.class);
	}

	@Test
	void test() {
		columnsList.add(2);
		List<Integer> columnList = gridModel.getParsysList();
		assertEquals(columnsList, columnList);
	}

}
