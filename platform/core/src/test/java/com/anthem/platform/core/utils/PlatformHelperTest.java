package com.anthem.platform.core.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Node;
import javax.jcr.Session;
import javax.xml.ws.Binding;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.handler.Handler;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.slf4j.Logger;

import com.adobe.acs.commons.genericlists.GenericList;
import com.anthem.platform.core.beans.ApiException;
import com.anthem.platform.core.beans.PostBackResponse;
import com.anthem.platform.core.constants.PlatformConstants;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.day.cq.replication.ReplicationOptions;
import com.day.cq.replication.Replicator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;

class PlatformHelperTest {

	@InjectMocks
	private PlatformHelper platformHelper;
	
	@Mock
	private Logger log;
	
	@Mock
	private ResourceResolverFactory resourceResolverFactory;
	
	@Mock
	private ResourceResolver resourceResolver;
	
	@Mock
	private Resource resource,original;
	
	@Mock
	private Asset asset;
	
	@Mock
	private InputStream inputStream;
	
	@Mock
    private Rendition rendition;
	
	@Mock
	private BindingProvider bindingProvider;
	
    @Mock
    private List<Handler> handlerChain;
	
	@Mock
	private Binding binding;
	
	@Mock
	private ReplicationOptions replicationoptions;
	
	@Mock
	private Replicator replicator;
	
	@Mock
	private Session session;
	
	@Mock
	private Node node,jsonnode;
	
	@Mock
	private PageManager pagemanager;
	
	@Mock
	private Page page;
	
	@Mock
	private GenericList list;
	
	@Mock
	private ResourceResolver resolver;
	
	@Mock
	private PostBackResponse postbackresponse=new PostBackResponse(PostBackResponse.Status.SUCCESS,200);
	
	@Mock
	private ApiException apiException;
			
	@Spy
    private SoapLogHandler soaploghandler;
	
    private String filepath="com/anthem/dam";
    
    private Map<String,Object> responseMap;
	
	@BeforeEach
	void setUp() throws Exception
	{		
		MockitoAnnotations.initMocks(this);
		inputStream = new ByteArrayInputStream("test data".getBytes());
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put(ResourceResolverFactory.SUBSERVICE, PlatformConstants.SUB_SERVICE_NAME);
		when(resourceResolverFactory.getServiceResourceResolver(paramMap)).thenReturn(resourceResolver);
		when(resourceResolver.getResource(filepath)).thenReturn(resource);
		when(resource.adaptTo(Asset.class)).thenReturn(asset);
		when(asset.getOriginal()).thenReturn(rendition);
		when(rendition.adaptTo(InputStream.class)).thenReturn(inputStream);
		when(bindingProvider.getBinding()).thenReturn(binding);
		when(binding.getHandlerChain()).thenReturn(handlerChain);
		when(resolver.adaptTo(Session.class)).thenReturn(session);
		when(session.itemExists("content/dam/anthem")).thenReturn(true);
		when(session.getNode("/dam")).thenReturn(node);
		when(node.addNode("anthem", JcrConstants.NT_UNSTRUCTURED)).thenReturn(jsonnode);
		when(jsonnode.getSession()).thenReturn(session);
		when(resolver.adaptTo(PageManager.class)).thenReturn(pagemanager);
		when(pagemanager.getPage("/dam/anthem")).thenReturn(page);
		when(page.adaptTo(GenericList.class)).thenReturn(list);
		
	}
	
	/**
	    * Test method for
	    * {@link com.anthem.platform.core.utils.PlatformHelper#getFileInputStream()}.
	   */ 
	@Test
	 void testGetFileInputStream() 
	{
	   InputStream actual= platformHelper.getFileInputStream(filepath, resourceResolver);
	    assertEquals(inputStream, actual);
	}
	
	@Test
	void testSetSoapLogger() 
	{
		platformHelper.setSoapLogger(bindingProvider);
		assertNotNull(platformHelper);
	}
	
	/**
	 * Test method for
	 * {@link com.anthem.platform.core.utils.ContentUtils#replicateContent()}.
	 */
	  @Test
	  void testReplicateContent() 
	  {
		  platformHelper.replicateContent("/content/dam/anthem", replicator, resolver);
	    assertNotNull(platformHelper);
	  }
	 
	  /**
		* Test method for
	    * {@link com.anthem.platform.core.utils.ContentUtils#deleteOldNodes()}.
	   */
	  @Test
	  void testDeleteOldNodes()
	  {
		  platformHelper.deleteOldNodes(resolver, "content/dam/anthem", JcrConstants.NT_UNSTRUCTURED);  
		 assertNotNull(platformHelper);
	  }
	  
	  /**
	    * Test method for
	    * {@link com.anthem.platform.core.utils.ContentUtils#getCSVFileType()}.
	   */ 
	  @Test 
	  void testGetCSVFileType()
	  {
		  platformHelper.getCSVFileType(resolver,"/dam/anthem","/dam");
	    assertNotNull(platformHelper);
	  }
	  
	  /**
		 * Test method for
		 * {@link com.anthem.platform.core.utils.CustomUtils#getDefaultSuccessPostBackResponse()}.
		 */
		 @Test
		 void testGetDefaultSuccessPostBackResponse() 
		 {
			postbackresponse=platformHelper.getDefaultSuccessPostBackResponse();
		    assertNotNull(platformHelper);
	      }
		 
		 /**
			 * Test method for
			 * {@link com.anthem.platform.core.utils.CustomUtils#getSuccessPostBackResponse()}.
			 */
			 @Test
			 void testGetSuccessPostBackResponse() 
			 {
				postbackresponse=platformHelper.getSuccessPostBackResponse(200, responseMap);
				assertNotNull(platformHelper);
		      }		

		 /**
		   * Test method for
		   * {@link com.anthem.platform.core.utils.CustomUtils#getErrorResponseFromApiException()}.
		 */
		  @Test
	   	  void testGetErrorResponseFromApiException() 
		  {
			postbackresponse=platformHelper.getErrorResponseFromApiException(200,apiException);
			assertNotNull(platformHelper);
		  }
		  
		  @Test
			void testLogMethodTime()
			{ platformHelper.logMethodTime(100);
			 assertNotNull(platformHelper);
			}


			/**
			 * Test method for
			 * {@link com.anthem.platform.core.utils.AnthemConnectorUtil#getStartTime()}.
			 */
			@Test
			void testGetStartTime()
			{  
				platformHelper.getStartTime();
			   assertNotNull(platformHelper);
			}



	 
}
