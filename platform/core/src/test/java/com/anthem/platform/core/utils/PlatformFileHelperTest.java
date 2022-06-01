package com.anthem.platform.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.Property;

import org.apache.http.message.BasicNameValuePair;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.beans.PostBackResponse;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.dam.commons.util.DamUtil;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

class PlatformFileHelperTest {

	@InjectMocks
	private PlatformFileHelper platformFileHelper;
	
	@Mock
	private ResourceResolver resolver;
	
	@Mock
	private ResourceResolverFactory resolverFactory;
	
	@Mock
	private Resource resource;
	
	@Mock
	private Asset asset;
	
	@Mock
	private Rendition ren;
	
	@Mock
	private InputStream inputStream;
		
	@Mock
	private Rendition rendition;
	
	@Mock
	private DamUtil damutil;
	
	@Mock
	private Node ntResourceNode;
	
	@Mock
	private Property nodeData;
	
	@Mock
	private Random random;
	
	@Mock
	private InputStream stream;
	
	@Mock
	private Binary binary;
	
	@Mock
	private SimpleFilterProvider filterProvider;
	
	@Mock
	private ApiException apiException;
	
	@Mock
	private Element link;
	
	@Mock
	private Document doc;
	
	@Mock
	private Elements links;
	
	@Mock
	private PostBackResponse postbackresponse=new PostBackResponse(PostBackResponse.Status.SUCCESS,200);
	
	private Map<String,Object> responseMap;
	
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
	

	private String filepath="com/anthem/dam";
	
	@BeforeEach
	void setUp() throws Exception
	{
		MockitoAnnotations.initMocks(this);
		inputStream = new ByteArrayInputStream("test data".getBytes());
		when(resolver.getResource(filepath)).thenReturn(resource);
		when(resource.adaptTo(Asset.class)).thenReturn(asset);
		when(asset.getOriginal()).thenReturn(ren);
		when(ren.adaptTo(InputStream.class)).thenReturn(inputStream);
		stream=new ByteArrayInputStream("test data".getBytes());
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE,"datareadwrite");
		when(resolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resolver);
		when(resolver.getResource("content/dam/anthem")).thenReturn(resource);
		when(resolver.getResource("content/dam/anthem/jcr:content")).thenReturn(resource);
		when(resource.adaptTo(Node.class)).thenReturn(ntResourceNode);
		when(ntResourceNode.getProperty(JcrConstants.JCR_DATA)).thenReturn(nodeData);
		  when(nodeData.getBinary()).thenReturn(binary);
		  when(binary.getStream()).thenReturn(stream);
		  when(resource.getResourceType()).thenReturn("dam:Asset");
		  when(resource.adaptTo(Asset.class)).thenReturn(asset);
		  when(asset.getRendition("dam.140.jpg")).thenReturn(rendition);
		  when(rendition.adaptTo(Resource.class)).thenReturn(resource);
		  params.add(new BasicNameValuePair("test", "main"));
		  when(link.html()).thenReturn("\\<p>\\");
	}


    /**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.PlatformFileHelper#getFileInputStream()}.
     */ 
	@Test
	void testGetFileInputStream()
	{
	   InputStream actual=platformFileHelper.getFileInputStream(filepath, resolver);
		assertEquals(inputStream, actual);
	}
	
	 /**
	  * Test method for
	  * {@link com.anthem.platform.core.utils.PlatformFileHelper#getRenditionResource()}.
	 */
	 @Test
	 void testGetRenditionResource()
	 {
		 platformFileHelper.getRenditionResource("content/dam/anthem","dam.140.jpg", resolver);
		 assertNotNull(platformFileHelper);
	 }
	 
	 /**
	   * Test method for
		* {@link com.anthem.platform.core.utils.PlatformFileHelper#readjcrData()}.
	  */
	 @Test 
	 void testReadjcrData() 
	 {
		 platformFileHelper.readjcrData("content/dam/anthem", resolver);
	    assertNotNull(platformFileHelper);
	 }
	
    /**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.PlatformFileHelper#readjcrDataAsString()}.
	   */
	 @Test 
	 void testReadjcrDataAsString()
	 { 
		String actual=platformFileHelper.readjcrDataAsString("content/dam/anthem",resolver);
		String expected="test data";
		 assertEquals(expected, actual);
	 }
	 

    /**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.PlatformFileHelper#readDataAsJson()}.
	   */ 
	 @Test
	 void testReadDataAsJson() 
	 {
		 platformFileHelper.readDataAsJson("content/dam/anthem",resolver);
		 assertNotNull(platformFileHelper);
	 }
	
	 /**
	   * Test method for
	   * {@link com.anthem.platform.core.utils.PlatformFileHelper#readjcrDataAsXML()}.
	   */  
	 @Test 
	 void testReadjcrDataAsXML() 
	 {
		 platformFileHelper.readjcrDataAsXML("content/dam/anthem", resolver);
		 assertNotNull(platformFileHelper);
	 }
	
	
}
