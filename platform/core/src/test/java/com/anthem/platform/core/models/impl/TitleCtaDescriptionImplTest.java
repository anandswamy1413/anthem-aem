package com.anthem.platform.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.models.TitleCtaDescription;
import com.anthem.platform.core.models.impl.TitleCtaDescriptionImpl.IconList;
import com.anthem.platform.core.services.utility.JacksonMapperService;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class TitleCtaDescriptionImplTest {

	private final AemContext ctx = new AemContext();

	@InjectMocks
	private TitleCtaDescriptionImpl titleCtaDescriptionImpl;

	@Mock
	TitleCtaDescription titleCtaDescription;

	@Mock
	Resource childRes;

	@InjectMocks
	IconList iconList;

	@Mock
	JacksonMapperService jacksonMapper;

	@Mock
	private List<IconList> ctaList;

	private String ctaLinks = "CTA Links";

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(jacksonMapper.convertObjectToJson(ctaList, false)).thenReturn(ctaLinks);
		ctx.addModelsForClasses(TitleCtaDescription.class);
		ctx.load().json("/com/anthem/platform/core/models/TitleCtaDescriptionTest.json", "/content");
		childRes = ctx.resourceResolver().getResource("/content/title-cta");
		titleCtaDescription = childRes.adaptTo(TitleCtaDescription.class);
		iconList = childRes.adaptTo(IconList.class);
	}

	@Test
	void testInit() throws Exception {
		Method method = TitleCtaDescriptionImpl.class.getDeclaredMethod("init");
		method.setAccessible(true);
		method.invoke(titleCtaDescriptionImpl);
		assertNotNull(titleCtaDescriptionImpl);
	}

	@Test
	void testGetId() {
		String actual = titleCtaDescription.getId();
		String expected = "Id";
		assertEquals(expected, actual);
	}

	@Test
	void testGetTitleField() {
		String actual = titleCtaDescription.getTitleField();
		String expected = "Title";
		assertEquals(expected, actual);
	}

	@Test
	void testGetType() {
		String actual = titleCtaDescription.getType();
		String expected = "Type";
		assertEquals(expected, actual);
	}

	@Test
	void testGetTitleFontWeight() {
		String actual = titleCtaDescription.getTitleFontWeight();
		String expected = "50%";
		assertEquals(expected, actual);
	}

	@Test
	void testGetTitleLetterSpacing() {
		String actual = titleCtaDescription.getTitleLetterSpacing();
		String expected = "true";
		assertEquals(expected, actual);
	}

	@Test
	void testGetTitleTextColor() {
		String actual = titleCtaDescription.getTitleTextColor();
		String expected = "red";
		assertEquals(expected, actual);
	}

	@Test
	void testGetLinkURL() {
		String actual = titleCtaDescription.getLinkURL();
		String expected = "content/dam/anthem/platform/demo";
		assertEquals(expected, actual);
	}

	@Test
	void testGetDescRequired() {
		String actual = titleCtaDescription.getDescRequired();
		String expected = "true";
		assertEquals(expected, actual);
	}

	@Test
	void testGetEnableBluebar() {
		String actual = titleCtaDescription.getEnableBluebar();
		String expected = "true";
		assertEquals(expected, actual);
	}

	@Test
	void testGetDescription() {
		String actual = titleCtaDescription.getDescription();
		String expected = "Description";
		assertEquals(expected, actual);
	}

	@Test
	void testGetDescriptionWeight() {
		String actual = titleCtaDescription.getDescriptionWeight();
		String expected = "20%";
		assertEquals(expected, actual);
	}

	@Test
	void testGetDescTextColor() {
		String actual = titleCtaDescription.getDescTextColor();
		String expected = "White";
		assertEquals(expected, actual);
	}

	@Test
	void testGetLinkTextColor() {
		String actual = titleCtaDescription.getLinkTextColor();
		String expected = "Black";
		assertEquals(expected, actual);
	}

	@Test
	void testGetCtaIcon() {
		String actual = iconList.getCtaIcon();
		String expected = "content/dam/anthem/platform/icon.png";
		assertEquals(expected, actual);
	}

	@Test
	void testGetCtaLabel() {
		String actual = iconList.getCtaLabel();
		String expected = "CTA Label";
		assertEquals(expected, actual);
	}

	@Test
	void testGetCtaLink() {
		String actual = iconList.getCtaLink();
		String expected = "content/dam/anthem/platform/demo";
		assertEquals(expected, actual);
	}

	@Test
	void testGetCtaAnalytics() {
		String actual = iconList.getCtaAnalytics();
		String expected = "CTA Analytics";
		assertEquals(expected, actual);
	}

	@Test
	void testGetOpenCtaInNewTab() {
		String actual = iconList.getOpenCtaInNewTab();
		String expected = "New Tab";
		assertEquals(expected, actual);
	}	

	@Test
	void testGetCtaLinks() {
		titleCtaDescription.getCtaLinks();
		assertNotNull(titleCtaDescription);
	}
	
}
