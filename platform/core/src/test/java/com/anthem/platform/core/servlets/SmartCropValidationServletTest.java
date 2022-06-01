package com.anthem.platform.core.servlets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.beans.ApiException;
import com.day.cq.dam.api.DamConstants;
import com.day.crx.JcrConstants;

class SmartCropValidationServletTest {

	private static final String IMAGE_PATH = "anthem/dam/asserts/desktopImage.JPEG";
	private static final String MOBILE_IMAGE_PATH = "anthem/dam/asserts/mobileImage.JPEG";
	private static final String CROP_PROFILE = "anthem/dam/asserts/cropImage.JPEG";
	private static final String MOBILE_CROP_PROFILE = "anthem/dam/asserts/cropImage.JPEG";
	private static final String SMART_CROP_PROP = "crop_type";
	private static final String BANNER = "banner";

	StringWriter stringWriter = new StringWriter();
	PrintWriter writer = new PrintWriter(stringWriter, false);

	@InjectMocks
	SmartCropValidationServlet smartCropValidationServlet;

	@Mock
	private SlingHttpServletRequest request;

	@Mock
	private SlingHttpServletResponse response;

	@Mock
	ResourceResolver resourceResolver;

	@Mock
	Resource assetRes;

	@Mock
	Resource folderRes;

	@Mock
	Resource jcrResource;

	@Mock
	private ValueMap valueMap;

	@Mock
	Resource profileRes;

	@Mock
	ValueMap map;

	@Mock
	ValueMap mobileMap;

	@BeforeEach
	public void setup() throws IOException {
		MockitoAnnotations.initMocks(this);
		when(request.getResourceResolver()).thenReturn(resourceResolver);
		when(request.getParameter("imgPath")).thenReturn(IMAGE_PATH);
		when(request.getParameter("mobileImgPath")).thenReturn(MOBILE_IMAGE_PATH);
		when(request.getParameter("cropProfile")).thenReturn(CROP_PROFILE);
		when(request.getParameter("mobileCropProfile")).thenReturn(MOBILE_CROP_PROFILE);
		when(resourceResolver.getResource(IMAGE_PATH)).thenReturn(assetRes);
		when(assetRes.getParent()).thenReturn(folderRes);
		when(folderRes.getChild(JcrConstants.JCR_CONTENT)).thenReturn(jcrResource);
		when(jcrResource.getValueMap()).thenReturn(valueMap);
		when(valueMap.get(DamConstants.IMAGE_PROFILE, String.class)).thenReturn("imgProfile");
		when(resourceResolver.getResource("imgProfile")).thenReturn(profileRes);
		when(profileRes.getValueMap()).thenReturn(map);
		when(map.get(BANNER, String.class)).thenReturn("anthem/dam/asserts/cropImage.JPEG,banner1|");
		when(map.get(SMART_CROP_PROP, String.class)).thenReturn("smartCrop");
		when(resourceResolver.getResource(MOBILE_IMAGE_PATH)).thenReturn(assetRes);
		when(response.getWriter()).thenReturn(writer);
	}

	@Test
	void testDoGet() throws Exception {
		smartCropValidationServlet.doGet(request, response);
		assertNotNull(smartCropValidationServlet);
	}

	@Test
	void testGetProfileMap() throws Exception {
		Class[] parameterTypes = new Class[2];
		parameterTypes[0] = ResourceResolver.class;
		parameterTypes[1] = String.class;
		Object[] parameters = new Object[2];
		parameters[0] = resourceResolver;
		parameters[1] = IMAGE_PATH;
		Method method = SmartCropValidationServlet.class.getDeclaredMethod("getProfileMap", parameterTypes);
		method.setAccessible(true);
		ValueMap result = (ValueMap) method.invoke(smartCropValidationServlet, parameters);
		assertNotNull(result);
		assertNotNull(smartCropValidationServlet);
	}
}
