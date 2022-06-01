package com.anthem.platform.core.services.utility.impl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.xml.stream.XMLStreamWriter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.services.utility.GenericListService;
import com.anthem.platform.core.services.utility.configs.AssetXmlFeedConfigs;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;

class AssetXmlFeedServiceImplTest {

	@InjectMocks
	AssetXmlFeedServiceImpl assetXmlFeedServiceImpl;

	@Mock
	AssetXmlFeedConfigs config;

	@Mock
	XMLStreamWriter stream;

	@Mock
	Asset asset;

	@Mock
	Map<String, String> configMap = new HashMap<>();

	@Mock
	Resource contentResource;

	@Mock
	Resource resource;

	@Mock
	Resource resourc;

	@Mock
	ValueMap properties;

	@Mock
	Map<String, String> thumbnailMap;

	@Mock
	GenericListService genericListService;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	void test() {
		assetXmlFeedServiceImpl.activate(config);
	}

	@Test
	void testWriteElement() throws Exception {
		Class[] parameterTypes = new Class[3];
		parameterTypes[0] = XMLStreamWriter.class;
		parameterTypes[1] = String.class;
		parameterTypes[2] = String.class;
		Object[] parameters = new Object[3];
		parameters[0] = stream;
		parameters[1] = "elementName";
		parameters[2] = "text";
		Method method = AssetXmlFeedServiceImpl.class.getDeclaredMethod("writeElement", parameterTypes);
		method.setAccessible(true);
		String result = (String) method.invoke(assetXmlFeedServiceImpl, parameters);
		assertNotNull(assetXmlFeedServiceImpl);
	}

	@Test
	void testGetAccessAPI() throws Exception {
		Class[] parameterTypes = new Class[2];
		parameterTypes[0] = Asset.class;
		parameterTypes[1] = Map.class;
		Object[] parameters = new Object[2];
		parameters[0] = asset;
		parameters[1] = configMap;
		when(configMap.get("includeOnlyTaggedAsset")).thenReturn("TRUE");
		when(asset.adaptTo(Resource.class)).thenReturn(resource);
		when(resource.getChild(JcrConstants.JCR_CONTENT)).thenReturn(resourc);
		when(resourc.getChild("medatdata")).thenReturn(contentResource);
		when(contentResource.getValueMap()).thenReturn(properties);
		when(properties.get("applicationTag", String.class)).thenReturn("applicationTag");
		Method method = AssetXmlFeedServiceImpl.class.getDeclaredMethod("isCrawlable", parameterTypes);
		method.setAccessible(true);
		method.invoke(assetXmlFeedServiceImpl, parameters);
		assertNotNull(assetXmlFeedServiceImpl);
	}

	@Test
	void testWriteImagePropertyValue() throws Exception {
		Class[] parameterTypes = new Class[4];
		parameterTypes[0] = XMLStreamWriter.class;
		parameterTypes[1] = String.class;
		parameterTypes[2] = String.class;
		parameterTypes[3] = ValueMap.class;
		Object[] parameters = new Object[4];
		parameters[0] = stream;
		parameters[1] = "elementName";
		parameters[2] = "propertyName";
		parameters[3] = properties;
		when(properties.get("propertyName", String.class)).thenReturn("value");
		when(genericListService.getGenericListAsMap("thumbnailListPath")).thenReturn(thumbnailMap);
		when(thumbnailMap.get("value")).thenReturn("/content/dam/demo");
		when(thumbnailMap.get("default")).thenReturn("/content/dam/demo");
		Method method = AssetXmlFeedServiceImpl.class.getDeclaredMethod("writeImagePropertyValue", parameterTypes);
		method.setAccessible(true);
		method.invoke(assetXmlFeedServiceImpl, parameters);
		assertNotNull(assetXmlFeedServiceImpl);
	}

	@Test
	void testWriteFirstPropertyValue() throws Exception {
		Class[] parameterTypes = new Class[5];
		parameterTypes[0] = XMLStreamWriter.class;
		parameterTypes[1] = String.class;
		parameterTypes[2] = String.class;
		parameterTypes[3] = ValueMap.class;
		parameterTypes[4] = Asset.class;
		Object[] parameters = new Object[5];
		parameters[0] = stream;
		parameters[1] = "elementName";
		parameters[2] = "propertyName";
		parameters[3] = properties;
		parameters[4] = asset;
		when(properties.get("propertyName", String.class)).thenReturn("value");
		Method method = AssetXmlFeedServiceImpl.class.getDeclaredMethod("writeFirstPropertyValue", parameterTypes);
		method.setAccessible(true);
		method.invoke(assetXmlFeedServiceImpl, parameters);
		assertNotNull(assetXmlFeedServiceImpl);
	}

}
