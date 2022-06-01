package com.anthem.platform.core.services.utility.impl;

import static org.mockito.Mockito.when;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.anthem.platform.core.services.utility.configs.PageXmlFeedConfigs;
import com.day.cq.commons.Externalizer;
import com.day.cq.wcm.api.Page;

class PageXmlFeedServiceImplTest {

	@InjectMocks
	PageXmlFeedServiceImpl pageXmlFeedServiceImpl;
	
	@Mock
	PageXmlFeedConfigs config;
	
	String[] includePageTemplates=new String[1];
	
	@Mock
	Page page;
	
	@Mock
	 XMLStreamWriter stream;
	
	@Mock
	SlingHttpServletRequest request;
	
	@Mock
	private transient Externalizer externalizer;
	
	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		includePageTemplates[0]="test";
		when(config.allowedTemplates()).thenReturn(includePageTemplates);
	}

	@Test
	void testGetActivate() {
		pageXmlFeedServiceImpl.activate(config);	
	}
	
	@Test
	void testWrite() throws XMLStreamException {
		when(config.allowedTemplates()).thenReturn(includePageTemplates);
		pageXmlFeedServiceImpl.write(page, stream, request, "NS", "externalizerKey");	
	}

	@Test
	void testExternalizeUri() throws XMLStreamException {
		when(externalizer.externalLink(request.getResourceResolver(),
				"externalizerKey", "/content/sites/demo")).thenReturn("sample");
		pageXmlFeedServiceImpl.externalizeUri(request,"/content/sites/demo","externalizerKey");	
	}
	
	@Test
	void testExternalizeUriElse() throws XMLStreamException {		
		pageXmlFeedServiceImpl.externalizeUri(request,"/content/sites/demo","");	
	}
	
	@Test
	void testWriteElement() throws XMLStreamException {	
		stream.writeStartElement("NS","elementName");
		stream.writeCharacters("text");
		stream.writeEndElement();
		pageXmlFeedServiceImpl.writeElement(stream,"elementName","text","NS");
	}
	
	@Test
	void testIsValidPageTemplate() throws XMLStreamException {		
		pageXmlFeedServiceImpl.isValidPageTemplate(page);
	}

}
