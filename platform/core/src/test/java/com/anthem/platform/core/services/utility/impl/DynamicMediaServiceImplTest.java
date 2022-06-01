package com.anthem.platform.core.services.utility.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.settings.SlingSettingsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.services.utility.configs.DynamicMediaServiceConfig;

class DynamicMediaServiceImplTest {

	private static final String DAM_PREFIX = "/content/dam";
	private static final String IS_IMAGE = "is/image";
	private static final String SLASH = "/";
	private static final String COLON = ":";
	private static final String DAM_METADATA = "jcr:content/metadata";
	private static final String SCENE7_DOMAIN = "dam:scene7Domain";
	private static final String SCENE7_FILE_NAME = "dam:scene7File";

	@InjectMocks
	DynamicMediaServiceImpl dynamicMediaServiceImpl;

	@Mock
	DynamicMediaServiceConfig config;

	@Mock
	private SlingSettingsService slingSettingsService;

	private static final String PATH = "/content/dam/anthem/content/demo";

	@Mock
	ResourceResolver resolver;

	@Mock
	Resource damRes;

	@Mock
	Resource metaRes;

	@Mock
	ValueMap vm;

	@BeforeEach
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		when(resolver.getResource(PATH)).thenReturn(damRes);
		when(damRes.getChild(DAM_METADATA)).thenReturn(metaRes);
		when(metaRes.getValueMap()).thenReturn(vm);
	}

	@Test
	void testGetGenericListPathForSites() throws Exception {
		Method method = DynamicMediaServiceImpl.class.getDeclaredMethod("isAuthor");
		method.setAccessible(true);
		method.invoke(dynamicMediaServiceImpl);
	}

	@Test
	void testActivate() {
		when(config.previewServerUrl()).thenReturn("https://s7test1.scene7.com/anthem/content/demo");
		when(config.defaultImagepreset()).thenReturn("defaultResponsive");
		dynamicMediaServiceImpl.activate(config);
	}

	@Test
	void testIsDamPath() {
		boolean result = dynamicMediaServiceImpl.isDamPath(PATH);
		assertTrue(result);
	}

	@Test
	void testIsDam() {
		boolean result = dynamicMediaServiceImpl.isDamPath("anthem/library/demo");
		assertFalse(result);
	}

	@Test
	void testGetAssetMetadataMap() {
		dynamicMediaServiceImpl.getAssetMetadataMap(PATH, resolver);
	}

	@Test
	void testGetScene7File() throws Exception {
		when(vm.get(SCENE7_FILE_NAME, String.class)).thenReturn("file");
		Class[] parameterTypes = new Class[1];
		parameterTypes[0] = ValueMap.class;
		Object[] parameters = new Object[1];
		parameters[0] = vm;
		Method method = DynamicMediaServiceImpl.class.getDeclaredMethod("getScene7File", parameterTypes);
		method.setAccessible(true);
		String result = (String) method.invoke(dynamicMediaServiceImpl, parameters);
		assertNotNull(result);
	}

	@Test
	void testGetScene7Url() throws Exception {
		when(vm.get(SCENE7_DOMAIN, String.class)).thenReturn("author");
		Class[] parameterTypes = new Class[2];
		parameterTypes[0] = ValueMap.class;
		parameterTypes[1] = boolean.class;
		Object[] parameters = new Object[2];
		parameters[0] = vm;
		parameters[1] = true;
		Method method = DynamicMediaServiceImpl.class.getDeclaredMethod("getScene7Url", parameterTypes);
		method.setAccessible(true);
		String result = (String) method.invoke(dynamicMediaServiceImpl, parameters);
		assertNotNull(result);
	}

	@Test
	void testGetScene7UrlNullCheck() throws Exception {
		Class[] parameterTypes = new Class[2];
		parameterTypes[0] = ValueMap.class;
		parameterTypes[1] = boolean.class;
		Object[] parameters = new Object[2];
		parameters[0] = vm;
		parameters[1] = false;
		Method method = DynamicMediaServiceImpl.class.getDeclaredMethod("getScene7Url", parameterTypes);
		method.setAccessible(true);
		String result = (String) method.invoke(dynamicMediaServiceImpl, parameters);
		assertNull(result);
	}

	@Test
	void testGetDMAssetUrlNullCheck() {
		String result = dynamicMediaServiceImpl.getDMAssetUrl("https://s7test1.scene7.com/anthem/content/demo.png",
				"defaultResponsive", "https://s7test1.scene7.com/anthem/content/demo.png", "defaultResponsive",
				resolver, true);
		assertNotNull(result);
	}

	@Test
	void testGetDMAssetUrl() throws Exception {
		when(vm.get(SCENE7_DOMAIN, String.class)).thenReturn("author");
		String result = dynamicMediaServiceImpl.getDMAssetUrl(PATH,
				"defaultResponsive", "https://s7test1.scene7.com/anthem/content/demo.png", "defaultResponsive",
				resolver, true);
		assertNotNull(result);
	}
}