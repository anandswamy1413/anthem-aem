package com.anthem.platform.core.models.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.models.SocialShareModel;
import com.anthem.platform.core.models.impl.SocialShareModelImpl.IconList;
import com.anthem.platform.core.services.utility.JacksonMapperService;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;


@ExtendWith(AemContextExtension.class)
class SocialShareModelImplTest {

	private final AemContext ctx = new AemContext();

	@InjectMocks
	private SocialShareModelImpl socialShareModelImpl;

	@Mock
	SocialShareModel socialShareModel;

	@Mock
	Resource resource;

	@InjectMocks
	IconList iconList;

	@Mock
	JacksonMapperService jacksonMapper;

	@Mock
	private List<IconList> socialIcons;

	private String socialIconLinks;

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(jacksonMapper.convertObjectToJson(socialIcons, false)).thenReturn(socialIconLinks);
		ctx.addModelsForClasses(SocialShareModel.class);
		ctx.load().json("/com/anthem/platform/core/models/SocialShareModelTest.json", "/content");
		resource = ctx.resourceResolver().getResource("/content/social-share");
		socialShareModel = resource.adaptTo(SocialShareModel.class);
		iconList = resource.adaptTo(IconList.class);
	}

	@Test
	void testInit() throws Exception {
		Method method = SocialShareModelImpl.class.getDeclaredMethod("init");
		method.setAccessible(true);
		method.invoke(socialShareModelImpl);
		assertNotNull(socialShareModelImpl);
	}
	
	@Test
	void testGetIconImg(){
		String actual=iconList.getIconImg();
		String expected="content/dam/anthem/platform/icon.png";
		assertEquals(expected,actual);
	}
	
	@Test
	void testGetLinkText(){
		String actual=iconList.getLinkText();
		String expected="Link Text";
		assertEquals(expected,actual);
	}
	
	@Test
	void testGetLinkUrl(){
		String actual=iconList.getLinkUrl();
		String expected="content/dam/anthem/platform/demo";
		assertEquals(expected,actual);
	}
	
	@Test
	void testGetOpenInNewTab(){
		String actual=iconList.getOpenInNewTab();
		String expected="New Tab";
		assertEquals(expected,actual);
	}

	@Test
	void testGetAnalyticsTag(){
		String actual=iconList.getAnalyticsTag();
		String expected="Analytics";
		assertEquals(expected,actual);
	}	
	
	@Test
	void testGetSocialIconLinks(){
		socialShareModel.getSocialIconLinks();
		assertNotNull(socialShareModel);		
	}

}
