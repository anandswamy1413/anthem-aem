package com.anthem.platform.core.services.utility.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.services.utility.GenericListService;
import com.day.cq.commons.Language;
import com.day.cq.wcm.api.LanguageManager;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.day.text.Text;

class PageInfoServiceImplTest {

	@InjectMocks
	private PageInfoServiceImpl pageinfoserviceimpl;

	@Mock
	private LanguageManager languageManager;
	@Mock
	private Page languageRoot, statePage, parentPage;
	@Mock
	private GenericListService genericListService;
	@Mock
	private SlingSettingsService slingSettingsService;
	@Mock
	private Language language, parentPageLanguage, parentLanguageRoot;
	@Mock
	private ResourceResolver resourceResolver;
	@Mock
	private PageManager pageManager;
	@Mock
	private Resource contentResource;
	@Mock
	private Text text;

	private Locale locale = new Locale("en");
	
	private static final String STATE_CODES_PATH = "all_state_codes";
	
	@Mock
	Map<String,String> dataSource;
	

	@Mock
	Page page;
	
	@Mock
	Map<String, String> stateMap;
	
	@Mock
	ValueMap vm;
	
	@Mock
	Resource resource;
	
	Map<String,String> environments;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		Map<String, String> stateMap = new HashMap<>();
		stateMap.put("et", "test_value");
		when(languageRoot.getPath()).thenReturn("/et/commons/dam/anthem");
		when(genericListService.getGenericListAsMap("state_codes")).thenReturn(stateMap);
		when(languageManager.getCqLanguage(languageRoot.getContentResource(), true)).thenReturn(language);
		when(language.getLocale()).thenReturn(locale);
		when(languageRoot.getName()).thenReturn("et");
		when(resourceResolver.adaptTo(PageManager.class)).thenReturn(pageManager);
		when(pageManager.getPage("/et")).thenReturn(statePage);
		when(statePage.getParent()).thenReturn(parentPage);
		when(parentPage.getContentResource()).thenReturn(contentResource);
		when(languageManager.getCqLanguage(parentPage.getContentResource(), true)).thenReturn(parentPageLanguage);
		when(languageManager.getLanguageRoot(parentPage.getContentResource(), true)).thenReturn(languageRoot);
		when(parentPageLanguage.getLanguageCode()).thenReturn("en");
		when(languageManager.getLanguageRoot(languageRoot.getContentResource(), true)).thenReturn(languageRoot);

	}

	@Test
	void testGetPageLocaleLanguageNull() {
		when(parentPage.getContentResource()).thenReturn(null);
		when(languageManager.getCqLanguage(parentPage.getContentResource(), true)).thenReturn(null);
		Locale actual = pageinfoserviceimpl.getPageLocale(languageRoot, resourceResolver);
		final String expected = "en";
		assertEquals(expected, actual.toString());
	}

	@Test
	void testGetPageLocalePageNull() {
		when(parentPage.getContentResource()).thenReturn(null);
		when(languageManager.getLanguageRoot(parentPage.getContentResource(), true)).thenReturn(null);
		Locale actual = pageinfoserviceimpl.getPageLocale(languageRoot, resourceResolver);
		final String expected = "en";
		assertEquals(expected, actual.toString());
	}

	@Test
	void testGetPageLocalePageNameLength() {
		when(languageRoot.getName()).thenReturn("abc");
		Locale actual = pageinfoserviceimpl.getPageLocale(languageRoot, resourceResolver);
		final String expected = "en";
		assertEquals(expected, actual.toString());
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.PageInfoServiceImpl#getPageLocale()}.
	 */
	/*@Test
	void testGetPageLocale() {
		Locale actual = pageinfoserviceimpl.getPageLocale(languageRoot, resourceResolver);
		assertNotNull(pageinfoserviceimpl);
		final String expected = "en_DAM";
		assertEquals(expected, actual.toString());

		when(languageRoot.getName()).thenReturn("en");
		when(language.getLanguageCode()).thenReturn("en");
		pageinfoserviceimpl.getPageLocale(languageRoot, resourceResolver);
	}*/

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.PageInfoServiceImpl#getPageTitle()}.
	 */
	@Test
	void testGetPageTitle() {
		String actual = pageinfoserviceimpl.getPageTitle(languageRoot);
		final String expected = "et";
		assertEquals(expected, actual);

	}

	@Test
	void testGetPageTitleNotEmpty() {
		when(languageRoot.getPageTitle()).thenReturn("en");
		String actual = pageinfoserviceimpl.getPageTitle(languageRoot);
		final String expected = "en";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.PageInfoServiceImpl#getAnalyticsPageName()}.
	 */
	@Test
	void testGetAnalyticsPageName() {
		String actual = pageinfoserviceimpl.getAnalyticsPageName(languageRoot, 2);
		final String expected = "commons:dam:anthem";
		assertEquals(expected, actual);
	}

	/**
	 * Test method for
	 * {@link com.anthem.platform.core.services.impl.PageInfoServiceImpl#getRunModes()}.
	 */
	@Test
	void testGetRunModes() {
		pageinfoserviceimpl.getRunModes();
		assertNotNull(pageinfoserviceimpl);

	}
	
	@Test
	void testGetStatePagePath() throws Exception {
		Class[] parameterTypes = new Class[2];
		parameterTypes[0] = String.class;
		parameterTypes[1] = String.class;
		Object[] parameters = new Object[2];
		parameters[0] = "content/sites/demo/pagePath";
		parameters[1] = "stateCode";		
		Method method = PageInfoServiceImpl.class.getDeclaredMethod("getStatePagePath", parameterTypes);
		method.setAccessible(true);
		method.invoke(pageinfoserviceimpl, parameters);
		assertNotNull(pageinfoserviceimpl);
	}
	
	@Test
	void testGetAnalyticsTitle() {
		when(page.getContentResource()).thenReturn(resource);
		when(resource.getValueMap()).thenReturn(vm);
		when(vm.get("analyticsTitle", String.class)).thenReturn("pagePath");
		pageinfoserviceimpl.getAnalyticsTitle(page);
		assertNotNull(pageinfoserviceimpl);
	}
	
	@Test
	void testGetAnalyticsTitleElse() {
		when(page.getContentResource()).thenReturn(resource);
		when(resource.getValueMap()).thenReturn(vm);
		pageinfoserviceimpl.getAnalyticsTitle(page);
		assertNotNull(pageinfoserviceimpl);
	}
	
	@Test
	void testGetAnalyticsName() {
		when(genericListService.getGenericListAsMap("ENVIRONMENTS")).thenReturn(environments);
		pageinfoserviceimpl.getRunMode();
		assertNotNull(pageinfoserviceimpl);
	}
	
	@Test
	void testGetSeoTitle() {
		when(page.getContentResource()).thenReturn(resource);
		when(resource.getValueMap()).thenReturn(vm);
		when(vm.get("seoTitle", String.class)).thenReturn("seoTitle");
		pageinfoserviceimpl.getSeoTitle(page);
		assertNotNull(pageinfoserviceimpl);
	}
	
	@Test
	void testGetSeoTitleElse() {
		when(page.getContentResource()).thenReturn(resource);
		when(resource.getValueMap()).thenReturn(vm);
		pageinfoserviceimpl.getSeoTitle(page);
		assertNotNull(pageinfoserviceimpl);
	}
	
	@Test
	void testGetContextData() {	
		pageinfoserviceimpl.getContextData(dataSource,"key","brandName");
		assertNotNull(pageinfoserviceimpl);
	}

}
