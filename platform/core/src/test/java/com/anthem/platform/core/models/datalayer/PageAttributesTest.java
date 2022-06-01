package com.anthem.platform.core.models.datalayer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.services.utility.GenericListService;
import com.anthem.platform.core.services.utility.PageInfoService;
import com.day.cq.wcm.api.Page;

import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class PageAttributesTest {

	@InjectMocks
	private PageAttributes pageAttributes;

	@Mock
	private Resource resource;
	@Mock
	private PageInfoService pageInfoService;
	@Mock
	private Locale pageLocale;
	@Mock
	private Page currentPage;
	@Mock
	private GenericListService genericListService;
	@Mock
	private ResourceResolver resourceResolver;

	Map<String, String> environments = new HashMap<>();

	private Map<String, String> getEnvironments() {
		environments.put("key1", "runMode1");
		environments.put("key2", "runMode2");
		return environments;
	}

	private Map<String, String> getEnvironments2() {
		environments.put("key1", "value1");
		environments.put("key2", "value2");
		return environments;
	}

	Set<String> runModes = new HashSet<>();

	private Set<String> getRunMode() {
		runModes.add("runMode1");
		runModes.add("runMode2");
		return runModes;
	}

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(pageInfoService.getPageLocale(currentPage, resourceResolver)).thenReturn(pageLocale);
		assertNotNull(pageLocale);
		when(pageLocale.toLanguageTag()).thenReturn("language");
		assertEquals("language", pageLocale.toLanguageTag());
		when(pageLocale.getCountry()).thenReturn("country");
		when(genericListService.getGenericListAsMap("environments")).thenReturn(getEnvironments());
		when(pageInfoService.getRunModes()).thenReturn(getRunMode());
		assertNotNull(pageAttributes);

	}

	@Test
	void testInit() throws Exception {
		Method method = PageAttributes.class.getDeclaredMethod("init");
		method.setAccessible(true);
		method.invoke(pageAttributes);
		assertNotNull(pageAttributes);
		final String expectedLang = "language";
		String actualLang = pageAttributes.getLanguage();
		assertEquals(expectedLang, actualLang);
		final String expectedCountry = "country";
		String actualCountry = pageAttributes.getCountry();
		assertEquals(expectedCountry, actualCountry);
		final String expectedErr = "";
		String actualErr = pageAttributes.getErrorType();
		assertEquals(expectedErr, actualErr);
		pageAttributes.getEnvironment();
	}

	@Test
	void testGetRunModes() throws NoSuchMethodException, SecurityException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		when(genericListService.getGenericListAsMap("environments")).thenReturn(getEnvironments2());
		Method method = PageAttributes.class.getDeclaredMethod("init");
		method.setAccessible(true);
		method.invoke(pageAttributes);
		assertNotNull(pageAttributes);
	}
	
	@Test
	void testGetPageStateCode() {
		when(pageInfoService.getPageStateCode(currentPage)).thenReturn(Mockito.anyString());
		pageAttributes.getPageStateCode();
		assertNotNull(pageAttributes);
	}
	
	@Test
	void testGetStateRedirectionUrl() {
		when(pageInfoService.getPageStateCode(currentPage)).thenReturn("stateCode");
		when(currentPage.getPath()).thenReturn("/home/stateCode/test");
		pageAttributes.getStateRedirectionUrl();
		assertNotNull(pageAttributes);
	}

}
