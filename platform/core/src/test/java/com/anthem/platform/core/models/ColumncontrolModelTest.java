package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class ColumncontrolModelTest {

	private final AemContext ctx = new AemContext();
	private Resource resource;
	
	@InjectMocks
    private ColumncontrolModel columncontrol;
	
	@BeforeEach
	void setUp() throws Exception {
		ctx.addModelsForClasses(ColumncontrolModel.class);
		ctx.load().json("/com/anthem/platform/core/models/ColumncontrolModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/columncontrol");
		columncontrol = resource.adaptTo(ColumncontrolModel.class);
	}
	
	@Test
	void testgetNumOfColumns() {
		int[] expected = {0, 1, 2, 3, 4, 5};
		int[] actual = columncontrol.getNumOfColumns();
		assertNotNull(columncontrol);
		assertEquals(expected.length, actual.length);
	}
	
	@Test
	void testgetParsysList() {
		final List<Integer> expected = new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3, 4, 5));
		List<Integer> actual = columncontrol.getParsysList();
		assertNotNull(columncontrol);
		assertEquals(expected, actual);
	}
}