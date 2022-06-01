package com.anthem.platform.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.day.cq.wcm.api.NameConstants;
import com.day.cq.wcm.api.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;

class PlatformUtilsTest {

	@InjectMocks
	private PlatformUtils platformUtils;
	
	@Mock
	private ResourceResolver resourceResolver;
	
	@Mock
	protected SlingHttpServletRequest slingHttpServletRequest;
	
	@Mock
	private Resource resource;
	
	@Mock
	private Element link;
	
	@Mock
	private Document doc;
	
	@Mock
	private Elements links;

	@Mock
	private Page currentPage;
	
	private String url="/content/dam/anthem";
	
	private List<BasicNameValuePair> params=new ArrayList<>();
	
	private String htmlRichText="<html>\r\n" + 
			"<head>\r\n" + 
			"<title>Test Page</title>\r\n" + 
			"</head>\r\n" + 
			"<body>\r\n" + 
			"<h1>Test Page</h1>\r\n" + 
			"<p>Test <b>code</b> </p>\r\n" + 
			"<ul>\r\n" + 
			"<li>The first item in test list</li>\r\n" + 
			"<li>The second item; <i>italicize</i> key words</li>\r\n" + 
			"</ul>\r\n" + 
			"<p><img src=\"http://www.mygifs.com/CoverImage.gif\" alt=\"A Great HTML Resource\"></p>\r\n" + 
			"<p>Finally, link to <a href=\"tel:\">another test page</a> website</p>\r\n" + 
			"<p><a href=\"tel://www.dummies.com/\">Web site</a></p>\r\n"+
			"</body>\r\n" + 
			"</html>";
	
	@BeforeEach
	void setUp() throws Exception
	{  MockitoAnnotations.initMocks(this);
	   when(resourceResolver.resolve(url)).thenReturn(resource);
	   when(resourceResolver.resolve("com/anthem")).thenReturn(resource);
	   when(resource.getResourceType()).thenReturn("cq:Page");
	   when(resourceResolver.getResource(url)).thenReturn(resource);
	   when(PlatformUtils.isInternalPageLink(url, resourceResolver)).thenReturn(true);
	   when(PlatformUtils.isInternalPageLink("com/anthem", resourceResolver)).thenReturn(true);
	   params.add(new BasicNameValuePair("test", "main"));
	   when(link.html()).thenReturn("\\<p>\\");
	}
	
	
	@Test
	void testIsInternalPageLink()
	{ 
		boolean actual=PlatformUtils.isInternalPageLink(url, resourceResolver);
	    assertTrue(actual);
	}
	
	
	 /*@Test
	 void testFormatUrlDamPath() 
	 {  
		 PlatformUtils.formatURL("/content/dam/anthem", resourceResolver,slingHttpServletRequest);
			assertNotNull(platformUtils);

	 }
	 
	 @Test
	 void testFormatUrl() 
	 {  
		 when(resourceResolver.resolve("/content/anthem")).thenReturn(resource);
		 when(resourceResolver.getResource("/content/anthem")).thenReturn(resource);
		 when(resource.adaptTo(Page.class)).thenReturn(currentPage);
		 when(resourceResolver.map("/content/anthem")).thenReturn("/content/anthem");
		 when(currentPage.getDepth()).thenReturn(7);
		 PlatformUtils.formatURL("/content/anthem", resourceResolver,slingHttpServletRequest);
		 
		 assertNotNull(platformUtils);
		 when(resourceResolver.getResource("/content/anthem")).thenReturn(null);
		 PlatformUtils.formatURL("/content/anthem", resourceResolver,slingHttpServletRequest);
		 
		 when(resourceResolver.map("/content/anthem")).thenReturn("/content/anthem/home.html");
		 PlatformUtils.formatURL("/content/anthem", resourceResolver,slingHttpServletRequest);
		 
		 when(resource.isResourceType(NameConstants.NT_PAGE)).thenReturn(false);
		 PlatformUtils.formatURL("/content/anthem", resourceResolver,slingHttpServletRequest);

	 }
	  	 
	 @Test
	 void testGetMappedPath() 
	 {
		 PlatformUtils.getMappedPath(resourceResolver, url);
			assertNotNull(platformUtils);

	 }
	 
	  *//**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.CustomUtils#getUrlParams()}.
	 *//*
	  @Test
  	  void testGetUrlParams() 
	  { final String expected="test=main";
		String actual=PlatformUtils.getUrlParams(params);
		assertEquals(expected, actual);
	  }

	  *//**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.CustomUtils#getAnthemFormattedPhoneNumber()}.
	 *//*
	  @Test
  	  void testGetAnthemFormattedPhoneNumber() 
	  { 
		  PlatformUtils.getAnthemFormattedPhoneNumber(htmlRichText);
		  assertNotNull(platformUtils);
	  }

		
	  *//**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.CustomUtils#getAnthemFormattedPhoneNumberAndTty()}.
	 *//*
	  @Test
  	  void testGetAnthemFormattedPhoneNumberAndTty() 
	  { 
		  PlatformUtils.getAnthemFormattedPhoneNumberAndTty(htmlRichText);
		  assertNotNull(platformUtils);
	  }
*/
}
