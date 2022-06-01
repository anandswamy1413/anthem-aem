package com.anthem.platform.core.models;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class URLFormatterModelTest {

	@InjectMocks
	URLFormatterModel urlFormatter;
	
	@Mock
	private ResourceResolver resourceResolver;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void testInit() throws Exception {
		urlFormatter.init();
		assertNotNull(urlFormatter);
	}
	
	@Test
	void testGetFormattedURL() {
		String actual = urlFormatter.getFormattedURL();
		assertNotNull(actual);
	}
}
