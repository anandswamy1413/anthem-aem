package com.anthem.platform.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.adobe.cq.sightly.WCMUsePojo;

class LinkCheckerTest {

	private static final String AUTHORED_LINK = "link";

	@InjectMocks
	LinkChecker linkChecker;

	@Mock
	ResourceResolver resourceResolver;

	@Mock
	WCMUsePojo wCMUsePojo;

	public static final String SLASH_CONTENT = "/content";
	public static final String SUFFIX_HTML = ".html";

	@BeforeEach
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		when(wCMUsePojo.getResourceResolver()).thenReturn(resourceResolver);
		when(wCMUsePojo.get(AUTHORED_LINK, String.class)).thenReturn("authoredLink");
	}

	@Test
	void testLinkProvider() {
		String actualLink = linkChecker.linkProvider(SLASH_CONTENT);
		String expectedLink = SLASH_CONTENT + SUFFIX_HTML;
		assertEquals(expectedLink, actualLink);
	}

	@Test
	void testGetLink() {
		String actualLink = linkChecker.getLink();
		assertNotNull(linkChecker);
	}
}
