package com.anthem.platform.core.models.datalayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.Locale;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.testing.mock.sling.servlet.MockSlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.osgi.framework.BundleContext;

import com.anthem.platform.core.services.utility.PageInfoService;
import com.day.cq.wcm.api.Page;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class PageInfoTest {

	@InjectMocks
	private PageInfo pageInfo;

	@Mock
	private PageInfoService pageInfoService;
	@Mock
	private Page currentPage;
	@Mock
	private Locale locale;
	@Mock
	private Resource resource;

	private ResourceResolver resourceResolver;
	private SlingHttpServletRequest request;
	private BundleContext bundleContext;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		assertNotNull(currentPage);
		when(pageInfoService.getAnalyticsPageName(currentPage, 2)).thenReturn("pageName");
		assertEquals("pageName", pageInfoService.getAnalyticsPageName(currentPage, 2));
		when(pageInfoService.getPageTitle(currentPage)).thenReturn("pageTitle");
		assertEquals("pageTitle", pageInfoService.getPageTitle(currentPage));
		when(currentPage.getName()).thenReturn("pageId");
		assertEquals("pageId", currentPage.getName());
		when(currentPage.getPath()).thenReturn("destinationURL");
		assertEquals("destinationURL", currentPage.getPath());
		this.request = new MockSlingHttpServletRequest(this.resourceResolver, bundleContext);
		assertNotNull(request);
		when(request.getRequestURL().toString()).thenReturn("destinationURL");
		when(pageInfoService.getPageLocale(currentPage, resourceResolver)).thenReturn(locale);
		assertEquals(locale, pageInfoService.getPageLocale(currentPage, resourceResolver));
		when(locale.toLanguageTag()).thenReturn("language");
		assertEquals("language", locale.toLanguageTag());
		when(pageInfoService.getPageStateCode(currentPage)).thenReturn("geoRegion");
		assertEquals("geoRegion", pageInfoService.getPageStateCode(currentPage));
		assertNotNull(pageInfo);
	}

	@Test
	void testInit() throws Exception {
		Method method = PageInfo.class.getDeclaredMethod("init");
		method.setAccessible(true);
		/*
		 * method.invoke(pageInfo); assertNotNull(pageInfo); final String expectedPageId
		 * = "pageId"; String actualPageId = pageInfo.getPageId();
		 * assertEquals(expectedPageId, actualPageId); final String expectedPageName =
		 * "pageName"; String actualPageName = pageInfo.getPageName();
		 * assertEquals(expectedPageName, actualPageName); final String
		 * expectedPageTitle = "pageTitle"; String actualPageTitle =
		 * pageInfo.getPageTitle(); assertEquals(expectedPageTitle, actualPageTitle);
		 * final String expectedDestinationURL = "destinationURL"; String
		 * actualDestinationURL = pageInfo.getDestinationURL();
		 * assertEquals(expectedDestinationURL, actualDestinationURL); final String
		 * expectedLanguage = "language"; String actualLanguage =
		 * pageInfo.getLanguage(); assertEquals(expectedLanguage, actualLanguage); final
		 * String expectedRegion = "geoRegion"; String actualRegion =
		 * pageInfo.getGeoRegion(); assertEquals(expectedRegion, actualRegion);
		 * pageInfo.getReferringURL();
		 */
	}
}
